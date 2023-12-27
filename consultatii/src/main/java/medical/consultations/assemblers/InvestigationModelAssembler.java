package medical.consultations.assemblers;

import medical.consultations.controllers.ConsultationController;
import medical.consultations.entities.Consultation;
import medical.consultations.entities.Investigation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class InvestigationModelAssembler {
    public static EntityModel<Investigation> toModel(Investigation investigation, Consultation consultation)
    {
        Link self = linkTo(methodOn(ConsultationController.class).getOneInvestigation(
                consultation.getIdPatient(),
                consultation.getIdDoctor(),
                consultation.getDate(),
                investigation.getId()
        )).withSelfRel();

        Link parent = linkTo(methodOn(ConsultationController.class).getInvestigations(
                consultation.getIdPatient(),
                consultation.getIdDoctor(),
                consultation.getDate()
        )).withRel("parent");

        return EntityModel.of(investigation, self, parent);
    }
}
