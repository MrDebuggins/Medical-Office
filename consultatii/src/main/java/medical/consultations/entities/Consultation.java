package medical.consultations.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document("consultations")
public class Consultation {
    @Id
    private String id;

    private String idPatient;

    private Long idDoctor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Bucharest")
    private Date date;

    public Diagnose getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
    }

    private Diagnose diagnose;

    private List<Investigation> investigations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setDate(Date date) {
        this.date = date;
    }


    public List<Investigation> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<Investigation> investigations) {
        this.investigations = investigations;
    }
}
