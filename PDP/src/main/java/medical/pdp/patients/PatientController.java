package pdp.medical.patients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController
{
    @Autowired
    private PatientsRepository repository;

    @Autowired
    private PatientModelAssembler assembler;

    @GetMapping("/patients/{id_user}")
    EntityModel<Patient> oneById(@PathVariable Long id_user)
    {
        Patient patient = repository.findByIdUser(id_user);
        return assembler.toModel(patient);
    }

    @GetMapping("/patients")
    List<EntityModel<Patient>> all()
    {
        return repository.findAll().stream().map(patient -> assembler.toModel(patient)).toList();
    }

    @PostMapping("/patients")
    EntityModel<Patient> create(@RequestBody Patient newPatient)
    {
        Patient patient = repository.save(newPatient);
        return assembler.toModel(patient);
    }

    @PutMapping("/patients/{idUser}")
    EntityModel<Patient> update(@RequestBody Patient newPatient, @PathVariable Long idUser)
    {
        Patient patient = repository.save(newPatient);
        return assembler.toModel(patient);
    }

    @DeleteMapping("/patients/{idUser}")
    void delete(@PathVariable Long idUser)
    {
        repository.delete(repository.findByIdUser(idUser));
    }
}
