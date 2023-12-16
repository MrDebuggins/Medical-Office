package medical.pdp.model_assemblers;

import medical.pdp.entities.Doctor;
import medical.pdp.controllers.DoctorController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DoctorModelAssembler implements RepresentationModelAssembler<Doctor, EntityModel<Doctor>> {
    @Override
    public EntityModel<Doctor> toModel(Doctor doctor) {
        return EntityModel.of(doctor,
                linkTo(methodOn(DoctorController.class).one(doctor.getIdDoctor())).withSelfRel(),
                linkTo(methodOn(DoctorController.class).all()).withRel("doctors"));
    }
}
