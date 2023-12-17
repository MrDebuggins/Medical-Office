package medical.pdp.model_assemblers;

import medical.pdp.controllers.PatientController;
import medical.pdp.entities.Appointment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {
    @Override
    public EntityModel<Appointment> toModel(Appointment appointment){
        Link self = linkTo(methodOn(PatientController.class).getFilteredAppointments(
                appointment.getId().getPatient(),
                appointment.getId().getDoctor(),
                appointment.getId().getDate().toString(),
                ""
        )).withSelfRel();

        Link parent = linkTo(methodOn(PatientController.class).getAllAppointments(appointment.getId().getPatient())).withRel("physicians");

        return EntityModel.of(appointment, self, parent);
    }
}
