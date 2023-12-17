package medical.pdp.model_assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import medical.pdp.entities.Patient;
import medical.pdp.controllers.PatientController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PatientModelAssembler implements RepresentationModelAssembler<Patient, EntityModel<Patient>>
{
    @Override
    public EntityModel<Patient> toModel(Patient patient)
    {
        return EntityModel.of(patient,
                linkTo(methodOn(PatientController.class).oneById(patient.getCNP())).withSelfRel(),
                linkTo(methodOn(PatientController.class).all()).withRel("patients"));
    }
}
