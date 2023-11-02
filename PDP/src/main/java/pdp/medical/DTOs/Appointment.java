package pdp.medical.DTOs;

import jakarta.persistence.Entity;

import java.sql.Date;

@Entity
public class Appointment {
    private int id_pacient;
    private int id_doctor;
    private Date date;
    private Status status;

    public Appointment(int id_pacient, int id_doctor, Date date, Status status) {
        this.id_pacient = id_pacient;
        this.id_doctor = id_doctor;
        this.date = date;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}