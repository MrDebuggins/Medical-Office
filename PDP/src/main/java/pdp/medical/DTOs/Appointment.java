package pdp.medical.DTOs;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "programari")
public class Appointment {
    @EmbeddedId
    AppointmentKey id;

    @ManyToOne
    @MapsId("id_pacient")
    @JoinColumn(name = "id_pacient")
    Pacient pacient;

    @ManyToOne
    @MapsId("id_doctor")
    @JoinColumn(name = "id_doctor")
    Doctor doctor;

    @Column(name = "status")
    private Status status;

    public Appointment(AppointmentKey id, Status status) {
        this.id = id;
        this.status = status;
    }

    public AppointmentKey getId() {
        return id;
    }

    public void setId(AppointmentKey id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

