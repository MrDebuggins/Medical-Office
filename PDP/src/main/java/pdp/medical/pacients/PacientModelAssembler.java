package pdp.medical.DTOs;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pdp.medical.controllers.PacientController;

@Component
public class PacientModelAssembler implements RepresentationModelAssembler<Pacient, EntityModel<Pacient>>
{
    @Override
    public EntityModel<Pacient> toModel(Pacient pacient)
    {
        return EntityModel.of(pacient,
                linkTo(methodOn(PacientController.class).one(Long.valueOf(0)))).
    }
}
