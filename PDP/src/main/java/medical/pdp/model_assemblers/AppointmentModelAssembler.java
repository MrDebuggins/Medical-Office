package medical.pdp.model_assemblers;

import medical.pdp.controllers.DoctorController;
import medical.pdp.controllers.PatientController;
import medical.pdp.entities.Appointment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {
    @Override
    public EntityModel<Appointment> toModel(Appointment appointment){
        Link self = linkTo(methodOn(DoctorController.class).getFilteredAppointments(
                appointment.getId().getPatient(),
                appointment.getId().getDoctor(),
                appointment.getId().getDate().toString().substring(0, 16),
                ""
        )).withSelfRel();

        Link parent = linkTo(methodOn(PatientController.class).getFilteredAppointments(appointment.getId().getPatient(),"","")).withRel("parent");

        Link consult = Link.of("http://localhost:8080/api/medical_office/patients/"
                + appointment.getId().getPatient()
                + "/physicians/" + appointment.getId().getDoctor()
                + "/" + appointment.getId().getDate() + "/consultation/").withRel("consultation");

        return EntityModel.of(appointment, self, parent);
    }
}
