package pdp.medical.pacients;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PacientModelAssembler implements RepresentationModelAssembler<Pacient, EntityModel<Pacient>>
{
    @Override
    public EntityModel<Pacient> toModel(Pacient pacient)
    {
        return EntityModel.of(pacient,
                linkTo(methodOn(PacientController.class).one("123")).withSelfRel(),
                linkTo(methodOn(PacientController.class).all()).withRel("pacients"));
    }
}
