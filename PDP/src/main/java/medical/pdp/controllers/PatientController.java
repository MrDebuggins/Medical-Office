package medical.pdp.controllers;

import jakarta.transaction.Transactional;
import medical.pdp.entities.Appointment;
import medical.pdp.entities.AppointmentKey;
import medical.pdp.entities.Patient;
import medical.pdp.model_assemblers.AppointmentModelAssembler;
import medical.pdp.model_assemblers.PatientModelAssembler;
import medical.pdp.repositories.AppointmentRepository;
import medical.pdp.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class PatientController
{
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientModelAssembler patientModelAssembler;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentModelAssembler appointmentModelAssembler;

    @GetMapping("/patients")
    public List<EntityModel<Patient>> all()
    {
        return patientRepository.findAll().stream().map(patient -> patientModelAssembler.toModel(patient)).toList();
    }

    @PutMapping("/patients")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Patient> create(@RequestBody Patient patient)
    {
        Patient checkP = patientRepository.findByCNP(patient.getCNP());
        if(checkP != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patient already exist");

        return patientModelAssembler.toModel(patientRepository.save(patient));
    }

    @GetMapping("/patients/{id}")
    public EntityModel<Patient> oneById(@PathVariable String id)
    {
        Patient patient = patientRepository.findByCNP(id);

        if(patient != null)
        {
            return patientModelAssembler.toModel(patient);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent user");
        }
    }

    @PutMapping("/patients/{id}")
    public EntityModel<Patient> update(@RequestBody Patient newPatient, @PathVariable String id)
    {
        Patient patient = patientRepository.findByCNP(id);
        if(patient != null)
        {
            newPatient.setCNP(id);
            patient = patientRepository.save(newPatient);
            return patientModelAssembler.toModel(patient);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent user");
        }
    }

    @DeleteMapping("/patients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id)
    {
        Patient patient = patientRepository.findByCNP(id);
        if(patient == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent user");

        patientRepository.setInactive(id);
    }

    @GetMapping("/patients/{id_patient}/physicians")
    public List<EntityModel<Appointment>> getAllAppointments(@PathVariable String id_patient)
    {
        List<Appointment> appointments = appointmentRepository.findByIdPatient(id_patient);
        return appointments
                .stream()
                .map(appointment -> appointmentModelAssembler.toModel(appointment))
                .toList();
    }

    @GetMapping("/patients/{id_patient}/physicians/{id_doctor}")
    public List<EntityModel<Appointment>> getFilteredAppointments(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = "") String type)
    {
        LocalDate localDate = LocalDate.now();
        Calendar c = Calendar.getInstance();

        List<Appointment> appointments = new ArrayList<Appointment>();
        switch (type){
            case "month":
                c.set(Calendar.MONTH, Integer.parseInt(date) - 1);
                c.set(Calendar.DAY_OF_MONTH, 1);
                Date start = c.getTime();
                c.set(Calendar.DAY_OF_MONTH, 31);
                Date end = c.getTime();

                appointments = appointmentRepository.findById_DateBetween(start, end);
                break;
            case "day":
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date));
                appointments = appointmentRepository.findById_Date(c.getTime());
                break;
            default:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

                AppointmentKey id = new AppointmentKey();
                id.setPatient(id_patient);
                id.setDoctor(id_doctor);
                id.setDate(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));

                Appointment appointment = appointmentRepository.findById(id);
                if(appointment != null)
                {
                    appointments.add(appointment);
                }

                break;
        }

        return appointments
                .stream()
                .map(appointment -> appointmentModelAssembler.toModel(appointment))
                .toList();
    }

    @PutMapping("/patients/{id_patient}/physicians")
    EntityModel<Appointment> createOrUpdate(
            @PathVariable String id_patient,
            @RequestBody Appointment appointment)
    {
        appointment.getId().setPatient(id_patient);
        appointment = appointmentRepository.save(appointment);
        return appointmentModelAssembler.toModel(appointment);
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/patients/{id_patient}/physicians/{id_doctor}")
    void delete(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @RequestParam(required = true)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date)
    {
        AppointmentKey id = new AppointmentKey();
        id.setPatient(id_patient);
        id.setDoctor(id_doctor);
        id.setDate(date);

        Appointment appointment = appointmentRepository.findById(id);
        Calendar c = Calendar.getInstance();
        if(appointment != null)
        {
            appointmentRepository.deleteById(id);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent appointment");
        }
    }
}
