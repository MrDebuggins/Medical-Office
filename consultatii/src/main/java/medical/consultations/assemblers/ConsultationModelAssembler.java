package medical.consultations.assemblers;

import medical.consultations.controllers.ConsultationController;
import medical.consultations.entities.Consultation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsultationModelAssembler implements RepresentationModelAssembler<Consultation, EntityModel<Consultation>> {
    @Override
    public EntityModel<Consultation> toModel(Consultation consultation)
    {
        Link self = linkTo(methodOn(ConsultationController.class).getOne(
                consultation.getIdPatient(),
                consultation.getIdDoctor(),
                consultation.getDate()
        )).withSelfRel();

        Link parent = Link.of("http://localhost:8080/api/medical_office/patients/" +
                consultation.getIdPatient() + "/physicians/" +
                consultation.getIdDoctor() + "?date=" +
                consultation.getDate().toInstant().atZone(ZoneId.of("EET")).
                        toString().replace('T', ' ').
                        substring(0, 16)).withRel("parent");

        return EntityModel.of(consultation, self,parent);
    }
}
