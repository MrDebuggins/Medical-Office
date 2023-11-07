package pdp.medical.pacients;

import jakarta.persistence.*;
import pdp.medical.DTOs.Appointment;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "pacients")
public class Pacient {
    @Id
    @Column(name = "cnp")
    private String CNP;

    @Column(name = "id_user")
    private Long id_user;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "born_date")
    private Date born;

    @Column(name = "is_active")
    private boolean is_active;

    @ManyToMany(mappedBy = "id_user")
    @JoinColumn(name = "last_name")
    private List<Appointment> appointments;

    public Pacient(String CNP, Long id, String last_name, String first_name, String email, String phone, Date born, boolean is_active, List<Appointment> appointments) {
        this.CNP = CNP;
        this.id_user = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
        this.born = born;
        this.is_active = is_active;
        this.appointments = appointments;
    }

    public Pacient(String cnp, Long id)
    {
        this.CNP = cnp;
        this.id_user = id;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
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

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}