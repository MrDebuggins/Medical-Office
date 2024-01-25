package medical.gateway.config;

import io.grpc.StatusRuntimeException;
import medical.gateway.entities.Doctor;
import medical.gateway.entities.Patient;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import medical.gateway.proto.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;


@Component
public class RoleFilterFunctions
{
    @Autowired
    private static IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub IDM_Server;

    @Autowired
    private static RestTemplate restClient;

    private static String adminPathRegex = "/api/medical_office/(patients|physicians)/\\w*$";

    private static String patientPathRegex1 = "/api/medical_office/patients(/\\w+)+";
    private static String patientPathRegex2 = "/api/medical_office/physicians/\\w*";

    private static String doctorPathRegex1 = "/api/medical_office/physicians(/\\w+)+";
    private static String doctorPathRegex2 = "/api/medical_office/patients/\\w*";

    public static HandlerFilterFunction<ServerResponse, ServerResponse> validateAccess()
    {
        return (request, next) ->
        {
            long id;
            int role;

            try
            {
                String token = request.headers().header("Authorization")
                        .toString().split(" ")[1];

                Main.Token idmToken = Main.Token.newBuilder().setToken(token).build();

                Main.IdentityResponse idmResp = IDM_Server.validate(idmToken);
                id = idmResp.getId();
                role = idmResp.getRole();
            }
            catch (StatusRuntimeException e) {return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();}
            catch (Exception e){return ServerResponse.status(HttpStatus.BAD_REQUEST).build();}

            String path = request.path();

            if(role == 0 && request.path().matches(adminPathRegex)) // admin
            {
                return next.handle(request);
            }
            else if(role == 1) // doctor
            {
                if(request.path().matches(doctorPathRegex1))
                {
                    try
                    {
                        String doctorId = request.path().split("/")[3];
                        ResponseEntity<Doctor> responseEntity = restClient
                                .getForEntity("localhost:8080/physicians/" + doctorId, Doctor.class);

                        if(responseEntity.getStatusCode() != HttpStatus.OK)
                        {
                            return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                        }

                        if(id != responseEntity.getBody().getIdUser())
                        {
                            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                        }

                        return next.handle(request);
                    }
                    catch (RestClientException e)
                    {
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }

                if(request.path().matches(doctorPathRegex2) && request.method() == HttpMethod.GET)
                {
                    return next.handle(request);
                }
            }
            else if(role == 2)
            {
                if(request.path().matches(patientPathRegex1))
                {
                    try
                    {
                        String patientId = request.path().split("/")[3];
                        ResponseEntity<Patient> responseEntity = restClient
                                .getForEntity("localhost:8080/patients/" + patientId, Patient.class);

                        if(responseEntity.getStatusCode() != HttpStatus.OK)
                        {
                            return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                        }

                        if(id != responseEntity.getBody().getIdUser())
                        {
                            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                        }

                        return next.handle(request);
                    }
                    catch (RestClientException e)
                    {
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }

                if(request.path().matches(patientPathRegex2) && request.method() == HttpMethod.GET)
                {
                    return next.handle(request);
                }
            }

            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        };
    }
}
