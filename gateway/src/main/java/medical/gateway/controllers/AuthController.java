package medical.gateway.controllers;

import io.grpc.ManagedChannelBuilder;
import medical.gateway.DTOs.DoctorDTO;
import medical.gateway.DTOs.LoginInfo;
import medical.gateway.DTOs.LoginResponse;
import medical.gateway.DTOs.PatientDTO;
import medical.gateway.entities.Doctor;
import medical.gateway.entities.Patient;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import medical.gateway.proto.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class AuthController
{
    private IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub IDM_Server
            = IdentityManagementServiceGrpc.newBlockingStub(ManagedChannelBuilder
            .forAddress("idm", 8080).usePlaintext().build());

    @Autowired
    private RestTemplate restClient;

    @PostMapping("/api/medical_office/login")
    ResponseEntity<?> login(@RequestBody LoginInfo loginInfo)
    {
        try
        {
            Main.Account acc = Main.Account.newBuilder()
                    .setLogin(loginInfo.login)
                    .setPassword(loginInfo.password)
                    .build();
            Main.Token token = IDM_Server.authorize(acc);

            Main.IdentityResponse response = IDM_Server.validate(token);

            if(response.getRole() == 1)
            {
                String doctor = restClient.getForObject("http://pdp:8080/api/medical_office/physicians?idUser=" + response.getId(), String.class);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.idUser = response.getId();
                loginResponse.role = response.getRole();
                loginResponse.token = token.getToken();
                loginResponse.entity = doctor;
                return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
            }

            if(response.getRole() == 2)
            {
                String patient = restClient.getForObject("http://pdp:8080/api/medical_office/patients?idUser=" + response.getId(), String.class);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.idUser = response.getId();
                loginResponse.role = response.getRole();
                loginResponse.token = token.getToken();
                loginResponse.entity = patient;
                return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
            }

            return new ResponseEntity<String>(token.getToken(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/medical_office/register", headers = {"Authorization"})
    ResponseEntity<?> registerDoctor(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody DoctorDTO doctor)
    {
        Main.IdentityResponse admin;

        // validate admin token
        try
        {
            Main.Token adminToken = Main.Token.newBuilder().setToken(token.split(" ")[1]).build();
            admin = IDM_Server.validate(adminToken);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // create user in IDM
        Main.IdentityResponse user;
        try
        {
            Main.Account idmDoctor = Main.Account.newBuilder()
                    .setLogin(doctor.login)
                    .setPassword(doctor.password)
                    .setRole(1)
                    .build();

            user = IDM_Server.register(idmDoctor);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create doctor in PDP
        try
        {
            Doctor doctorEntity = new Doctor();
            doctorEntity.setIdUser(user.getId());
            doctorEntity.setFirstname(doctor.firstname);
            doctorEntity.setLastname(doctor.lastname);
            doctorEntity.setEmail(doctor.email);
            doctorEntity.setPhone(doctor.phone);
            doctorEntity.setSpecialization(doctor.specialization);
            ResponseEntity response = restClient.postForEntity("http://pdp:8080/api/medical_office/physicians/", doctorEntity, Object.class);

            if(response.getStatusCode() != HttpStatus.CREATED)
            {
                Main.Account idmDoctor = Main.Account.newBuilder()
                        .setLogin(doctor.login)
                        .build();
                IDM_Server.deleteUser(idmDoctor);

                return new ResponseEntity<>(response.getStatusCode());
            }
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/medical_office/register")
    ResponseEntity<?> registerPatient(@RequestBody PatientDTO patient)
    {
        Main.IdentityResponse user;
        // create user in IDM
        try
        {
            Main.Account idmDoctor = Main.Account.newBuilder()
                    .setLogin(patient.login)
                    .setPassword(patient.password)
                    .setRole(2)
                    .build();

            user = IDM_Server.register(idmDoctor);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create patient in PDP
        try
        {
            Patient patientEntity = new Patient();
            patientEntity.setIdUser(user.getId());
            patientEntity.setCNP(patient.cnp);
            patientEntity.setFirstname(patient.firstname);
            patientEntity.setLastname(patient.lastname);
            patientEntity.setEmail(patient.email);
            patientEntity.setPhone(patient.phone);
            patientEntity.setBorn(patient.born);
            patientEntity.setActive(patient.active);
            ResponseEntity response = restClient.postForEntity("http://pdp:8080/api/medical_office/patients/", patientEntity, Object.class);

            if(response.getStatusCode() != HttpStatus.CREATED)
            {
                Main.Account idmPatient = Main.Account.newBuilder()
                        .setLogin(patient.login)
                        .build();
                IDM_Server.deleteUser(idmPatient);

                return new ResponseEntity<>(response.getStatusCode());
            }
        }
        catch (Exception ex)
        {
            Main.Account idmPatient = Main.Account.newBuilder()
                    .setLogin(patient.login)
                    .build();
            IDM_Server.deleteUser(idmPatient);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
