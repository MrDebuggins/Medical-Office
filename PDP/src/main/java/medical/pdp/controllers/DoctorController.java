package medical.pdp.controllers;

import medical.pdp.entities.Doctor;
import medical.pdp.model_assemblers.DoctorModelAssembler;
import medical.pdp.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository repository;

    @Autowired
    private DoctorModelAssembler assembler;

    @GetMapping("/doctors/{id_doctor}")
    public EntityModel<Doctor> one(@PathVariable Long id_doctor){
        Doctor doctor = repository.findByIdDoctor(id_doctor);
        return assembler.toModel(doctor);
    }

    @GetMapping("/doctors")
    public List<EntityModel<Doctor>> all(){
        return repository.findAll().stream().map(doctor -> assembler.toModel(doctor)).toList();
    }
}
