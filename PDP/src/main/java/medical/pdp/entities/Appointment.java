package medical.pdp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "programari")
public class Appointment {
    @EmbeddedId
    AppointmentKey id;

    @ManyToOne
    @MapsId("id_pacient")
    @JoinColumn(name = "id_pacient")
    Patient patient;

    @ManyToOne
    @MapsId("id_doctor")
    @JoinColumn(name = "id_doctor")
    Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

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

