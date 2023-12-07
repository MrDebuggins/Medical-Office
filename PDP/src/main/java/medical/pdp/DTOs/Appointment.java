package pdp.medical.DTOs;

import jakarta.persistence.*;
import pdp.medical.doctors.Doctor;
import pdp.medical.patients.Patient;
import pdp.medical.patients.Status;

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

