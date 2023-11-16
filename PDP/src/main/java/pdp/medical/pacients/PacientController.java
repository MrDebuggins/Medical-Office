package pdp.medical.pacients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import pdp.medical.DTOs.Pacient;

import java.util.List;

@RestController
public class PacientController
{
    @Autowired
    private PacientsRepository repository;

    @Autowired
    private PacientModelAssembler assembler;

    @GetMapping("/pacients/{cnp}")
    EntityModel<Pacient> one(@PathVariable String cnp)
    {
        Pacient pacient = repository.findByCNP(cnp);
        return assembler.toModel(pacient);
    }

    @GetMapping("/pacients")
    List<EntityModel<Pacient>> all()
    {
        return null;
    }

    @PostMapping("/pcaients")
    EntityModel<Pacient> create(@RequestParam String cnp, @RequestParam Long id)
    {
        Pacient pacient = new Pacient(cnp, id);
        repository.save(pacient);
        return assembler.toModel(pacient);
    }
}
