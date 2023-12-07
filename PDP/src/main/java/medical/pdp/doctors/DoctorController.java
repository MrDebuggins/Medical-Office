package pdp.medical.doctors;

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
    EntityModel<Doctor> one(@PathVariable Long id_doctor){
        Doctor doctor = repository.findByIdDoctor(id_doctor);
        return assembler.toModel(doctor);
    }

    @GetMapping("/doctors")
    List<EntityModel<Doctor>> all(){
        return repository.findAll().stream().map(doctor -> assembler.toModel(doctor)).toList();
    }
}
