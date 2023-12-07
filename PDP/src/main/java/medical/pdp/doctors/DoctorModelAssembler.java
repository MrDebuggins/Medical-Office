package pdp.medical.doctors;

import jakarta.persistence.Column;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pdp.medical.patients.Patient;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DoctorModelAssembler implements RepresentationModelAssembler<Doctor, EntityModel<Doctor>> {
    @Override
    public EntityModel<Doctor> toModel(Doctor doctor) {
        return EntityModel.of(doctor,
                linkTo(methodOn(DoctorController.class).one(doctor.getId_doctor())).withSelfRel(),
                linkTo(methodOn(DoctorController.class).all()).withRel("doctors"));
    }
}
