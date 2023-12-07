package pdp.medical.doctors;

import jakarta.persistence.*;
import pdp.medical.DTOs.Appointment;

import java.util.Set;

@Entity
@Table(name = "doctori")
public class Doctor {
    @Id
    @Column(name = "id_doctor")
    private Long id_doctor;

    @Column(name = "id_user")
    private Long id_user;

    @Column(name = "nume")
    private String last_name;

    @Column(name = "prenume")
    private String first_name;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon")
    private String phone;

    @Column(name = "specializare")
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;

    public Doctor(Long id_doctor, Long id_user, String last_name, String first_name, String email, String phone, Specialization specialization, Set<Appointment> appointments) {
        this.id_doctor = id_doctor;
        this.id_user = id_user;
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.appointments = appointments;
    }

    public Doctor(Long id_user, String last_name, String first_name, String email, String phone, Specialization specialization) {
        this.id_user = id_user;
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    public Long getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(Long id_doctor) {
        this.id_doctor = id_doctor;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}