package medical.consultations.controllers;


import medical.consultations.entities.Consultation;
import medical.consultations.repository.IConsultationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ConsultationController {
    @Autowired
    private IConsultationsRepository repository;

    @GetMapping("/consultations")
    List<Consultation> all(){
        return repository.findAll();
    }

    @PutMapping("/consultations")
    Consultation create(@RequestBody Consultation consultation){
        return repository.save(consultation);
    }
}
