package medical.pdp.controllers;

import medical.pdp.entities.*;
import medical.pdp.model_assemblers.AppointmentModelAssembler;
import medical.pdp.model_assemblers.DoctorModelAssembler;
import medical.pdp.repositories.AppointmentRepository;
import medical.pdp.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorModelAssembler doctorModelAssembler;

    @Autowired
    private PagedResourcesAssembler<Doctor> doctorPagedAssembler;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentModelAssembler appointmentModelAssembler;

    @GetMapping("/physicians/{id_doctor}")
    public EntityModel<Doctor> one(@PathVariable Long id_doctor)
    {
        Doctor doctor = doctorRepository.findByIdDoctor(id_doctor);

        if(doctor == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent doctor");

        return doctorModelAssembler.toModel(doctor);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/physicians")
    public EntityModel<Doctor> create(@RequestBody Doctor doctor)
    {
        Doctor checkD = doctorRepository.findByIdDoctor(doctor.getIdDoctor());

        if(checkD != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor already exist");

        return doctorModelAssembler.toModel(doctorRepository.save(doctor));
    }

    @PutMapping("/physicians/{id_doctor}")
    public EntityModel<Doctor> update(@RequestBody Doctor doctor, @PathVariable Long id_doctor)
    {
        if(doctorRepository.findByIdDoctor(doctor.getIdDoctor()) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent doctor");

        doctor.setIdDoctor(id_doctor);
        return doctorModelAssembler.toModel(doctorRepository.save(doctor));
    }

    @GetMapping("/physicians")
    public PagedModel<EntityModel<Doctor>> paged(
            @RequestParam(required = false, name = "specialization") Specialization s,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer items)
    {
        page = (page == null)? 0:page;
        items = (items == null)? 3:items;
        PageRequest pageable = PageRequest.of(page, items);

        Page<Doctor> result;
        if(s == null && name == null)
        {
            result = doctorRepository.findAll(pageable);
        }
        else if (s == null)
        {
            result = doctorRepository.findByLastnameContains(name, pageable);
        }
        else if (name == null)
        {
            result = doctorRepository.findBySpecialization(s, pageable);
        }
        else
        {
            result = doctorRepository.findBySpecializationAndLastnameContains(s, name, pageable);
        }


        //return result.stream().map(doctor -> doctorModelAssembler.toModel(doctor)).toList();
        return doctorPagedAssembler.toModel(result, doctorModelAssembler);
    }

    @GetMapping("/physicians/{id_doctor}/patients")
    public List<EntityModel<Appointment>> getAllAppointments(@PathVariable Long id_doctor)
    {
        List<Appointment> appointments = appointmentRepository.findByIdDoctor(id_doctor);
        return appointments
                .stream()
                .map(appointment -> appointmentModelAssembler.toModel(appointment))
                .toList();
    }

    @GetMapping("/physicians/{id_doctor}/patients/{id_patient}")
    public List<EntityModel<Appointment>> getFilteredAppointments(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = "") String type)
    {
        LocalDate localDate = LocalDate.now();
        Calendar c = Calendar.getInstance();

        List<Appointment> appointments = new ArrayList<Appointment>();
        switch (type) {
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
                if (appointment != null) {
                    appointments.add(appointment);
                }

                break;
        }

        return appointments
                .stream()
                .map(appointment -> appointmentModelAssembler.toModel(appointment))
                .toList();
    }

    @PutMapping("/physicians/{id_doctor}/patients/{id_patient}")
    EntityModel<Appointment> createOrUpdate(
            @PathVariable Long id_doctor,
            @PathVariable String id_patient,
            @RequestParam(required = true)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @RequestBody String status)
    {
        AppointmentKey id = new AppointmentKey();
        id.setPatient(id_patient);
        id.setDoctor(id_doctor);
        id.setDate(date);

        Appointment appointment = appointmentRepository.findById(id);
        if(appointment == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nonexistent appointment");

        appointment.setStatus(Status.valueOf(status));
        appointment = appointmentRepository.save(appointment);
        return appointmentModelAssembler.toModel(appointment);
    }


}
