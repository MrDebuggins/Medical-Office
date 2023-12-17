package medical.pdp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AppointmentKey implements Serializable {
    @Column(name = "id_pacient")
    private String patient;

    @Column(name = "id_doctor")
    private Long doctor;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "data")
    private Date date;

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Long getDoctor() {
        return doctor;
    }

    public void setDoctor(Long doctor) {
        this.doctor = doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
