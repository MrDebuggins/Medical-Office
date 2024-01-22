package medical.consultations.controllers;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import medical.consultations.assemblers.ConsultationModelAssembler;
import medical.consultations.assemblers.InvestigationModelAssembler;
import medical.consultations.entities.Consultation;
import medical.consultations.entities.Investigation;
import medical.consultations.repository.IConsultationsRepository;
import medical.consultations.repository.MongoDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;


@RestController
public class ConsultationController {
    @Autowired
    private IConsultationsRepository repository;

    @Autowired
    private ConsultationModelAssembler consultationModelAssembler;

    @Autowired
    MongoOperations template;

    @GetMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/")
    public EntityModel<Consultation> getOne(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date)
    {
        Consultation consultation = repository.findByIdPatientAndIdDoctorAndDate(id_patient, id_doctor, date);
        if(consultation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        consultation.getInvestigations().clear();
        return consultationModelAssembler.toModel(consultation);
    }

    @PutMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/")
    public ResponseEntity<EntityModel<Consultation>> create(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @RequestBody Consultation consultation)
    {
        Consultation check = repository.findByIdPatientAndIdDoctorAndDate(id_patient, id_doctor, date);
        if(check != null)
        {
            var t = template.update(Consultation.class).matching(new Criteria().andOperator(
                    Criteria.where("idPatient").is(id_patient),
                    Criteria.where("idDoctor").is(id_doctor),
                    Criteria.where("date").is(date)
            )).apply(new Update().set("diagnose", consultation.getDiagnose())).all();

            check.setDiagnose(consultation.getDiagnose());
            return new ResponseEntity<>(consultationModelAssembler.toModel(check), HttpStatus.OK);
        }

        consultation.setIdPatient(id_patient);
        consultation.setIdDoctor(id_doctor);
        consultation.setDate(date);

        EntityModel<Consultation> model = consultationModelAssembler.toModel(repository.save(consultation));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @GetMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/investigations")
    public List<EntityModel<Investigation>> getInvestigations(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date)
    {
        Consultation consultation = repository.findByIdPatientAndIdDoctorAndDate(id_patient, id_doctor, date);
        if(consultation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        consultation.setIdPatient(id_patient);
        consultation.setIdDoctor(id_doctor);
        consultation.setDate(date);

        return consultation
                .getInvestigations()
                .stream()
                .map(investigation -> InvestigationModelAssembler.toModel(investigation, consultation))
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/investigations")
    public EntityModel<Investigation> createInvestigation(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @RequestBody Investigation investigation)
    {
        Consultation consultation = repository.findByIdPatientAndIdDoctorAndDate(id_patient, id_doctor, date);
        if(consultation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        var t = template.update(Consultation.class).matching(new Criteria().andOperator(
                Criteria.where("idPatient").is(id_patient),
                Criteria.where("idDoctor").is(id_doctor),
                Criteria.where("date").is(date)
        )).apply(new Update().push("investigations", investigation)).all();

        consultation.setIdPatient(id_patient);
        consultation.setIdDoctor(id_doctor);
        consultation.setDate(date);
        return InvestigationModelAssembler.toModel(investigation, consultation);
    }

    @GetMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/investigations/{id_investigation}")
    public EntityModel<Investigation> getOneInvestigation(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @PathVariable String id_investigation)
    {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("idPatient").is(id_patient),
                        Criteria.where("idDoctor").is(id_doctor),
                        Criteria.where("date").is(date)
                )),
                Aggregation.unwind("investigations"),
                Aggregation.match(Criteria.where("investigations._id").is(id_investigation)),
                Aggregation.replaceRoot("investigations")
        );

        Investigation result = template.aggregate(agg, Consultation.class,Investigation.class).getUniqueMappedResult();
        if(result == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else
        {
            Consultation consultation = new Consultation();
            consultation.setIdPatient(id_patient);
            consultation.setIdDoctor(id_doctor);
            consultation.setDate(date);
            return InvestigationModelAssembler.toModel(result, consultation);
        }
    }

    @PutMapping("/patients/{id_patient}/physicians/{id_doctor}/{date}/consultation/investigations/{id_investigation}")
    public EntityModel<Investigation> updateInvestigation(
            @PathVariable String id_patient,
            @PathVariable Long id_doctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @PathVariable String id_investigation,
            @RequestBody Investigation investigation
    )
    {
        investigation.setId(id_investigation);

        var t = template.update(Consultation.class).matching(new Criteria().andOperator(
                Criteria.where("idPatient").is(id_patient),
                Criteria.where("idDoctor").is(id_doctor),
                Criteria.where("date").is(date),
                Criteria.where("investigations._id").is(id_investigation)
        )).apply(new Update().set("investigations.$", investigation)).all();

        Consultation consultation = new Consultation();
        consultation.setIdPatient(id_patient);
        consultation.setIdDoctor(id_doctor);
        consultation.setDate(date);
        return InvestigationModelAssembler.toModel(investigation, consultation);
    }
}
