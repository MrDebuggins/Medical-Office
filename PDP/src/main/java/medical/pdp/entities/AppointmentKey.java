package medical.pdp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.sql.Date;

@Embeddable
public class AppointmentKey implements Serializable {
    @Column(name = "id_pacient")
    private int id_pacient;

    @Column(name = "id_doctor")
    private int id_doctor;

    @Column(name = "data")
    private Date date;

    public AppointmentKey(int id_pacient, int id_doctor, Date date) {
        this.id_pacient = id_pacient;
        this.id_doctor = id_doctor;
        this.date = date;
    }

    public int getId_pacient() {
        return id_pacient;
    }

    public void setId_pacient(int id_pacient) {
        this.id_pacient = id_pacient;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
