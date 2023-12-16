package medical.pdp.controllers;

import medical.pdp.entities.Patient;
import medical.pdp.model_assemblers.PatientModelAssembler;
import medical.pdp.repositories.PatientsRepository;
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

    @GetMapping("/patients/{id}")
    public EntityModel<Patient> oneById(@PathVariable Long id)
    {
        Patient patient = repository.findByIdUser(id);

        if(patient != null)
        {
            return assembler.toModel(patient);
        }
        else
        {
            return null; //TODO exception handling
        }
    }

    @GetMapping("/patients")
    public List<EntityModel<Patient>> all()
    {
        return repository.findAll().stream().map(patient -> assembler.toModel(patient)).toList();
    }

    @PutMapping("/patients/{id}")
    public EntityModel<Patient> update(@RequestBody Patient newPatient, @PathVariable Long id)
    {
        Patient patient = repository.save(newPatient);
        return assembler.toModel(patient);
    }

    @DeleteMapping("/patients/{id}")
    public void delete(@PathVariable Long id)
    {
        repository.setInactive(id);
    }
}
