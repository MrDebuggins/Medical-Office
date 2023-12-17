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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            return null; //TODO exception handling
        }
    }

    @PutMapping("/patients/{id}")
    public EntityModel<Patient> update(@RequestBody Patient newPatient, @PathVariable Long id)
    {
        Patient patient = patientRepository.save(newPatient);
        return patientModelAssembler.toModel(patient);
    }

    @DeleteMapping("/patients/{id}")
    public void delete(@PathVariable Long id)
    {
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

        List<Appointment> appointments = Collections.emptyList();
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
                String[] values = date.split("-");
                int v0 = Integer.parseInt(values[0]);
                int v1 = Integer.parseInt(values[1]);
                int v2 = Integer.parseInt(values[2]);

                if(values[0].length() <= 2)
                {
                    c.set(v2, v1 - 1, v0);
                }
                else
                {
                    c.set(v0,v1 - 1, v2);
                }

                appointments = appointmentRepository.findById_Date(c.getTime());

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

        appointmentRepository.deleteById(id);
    }
}
