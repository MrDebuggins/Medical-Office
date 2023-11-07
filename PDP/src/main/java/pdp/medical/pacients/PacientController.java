package pdp.medical.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pdp.medical.DTOs.Pacient;

@RestController
public class PacientController
{
    @GetMapping("/pacient/{id}")
    Pacient one(@PathVariable Long id)
    {
        return null;
    }
}
