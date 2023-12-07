package pdp.medical.patients;

import jakarta.persistence.*;
import pdp.medical.DTOs.Appointment;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "pacienti")
public class Patient {
    @Id
    @Column(name = "cnp")
    private String CNP;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "nume")
    private String lastname;

    @Column(name = "prenume")
    private String firstname;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon")
    private String phone;

    @Column(name = "nascut")
    private Date born;

    @Column(name = "activ")
    private boolean isActive = true;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments;

    public Patient(String CNP, Long id, String lastname, String firstname, String email, String phone, Date born, boolean isActive, Set<Appointment> appointments) {
        this.CNP = CNP;
        this.idUser = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.born = born;
        this.isActive = isActive;
        this.appointments = appointments;
    }

    public Patient(String CNP, Long idUser, String lastname, String firstname, String email, String phone, Date born) {
        this.CNP = CNP;
        this.idUser = idUser;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.born = born;
    }

    public Patient(){}

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}