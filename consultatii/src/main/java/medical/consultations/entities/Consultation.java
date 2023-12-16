package medical.consultations.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("appointments")
public class Consultation {
    @Id
    private String id;

    private Long idPatient;

    private Long idDoctor;

    private Date date;

    private List<Investigations> investigations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Long idPatient) {
        this.idPatient = idPatient;
    }

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public List<Investigations> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<Investigations> investigations) {
        this.investigations = investigations;
    }
}
