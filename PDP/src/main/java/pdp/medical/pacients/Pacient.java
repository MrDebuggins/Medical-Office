package pdp.medical.DTOs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;
import java.util.List;

@Entity
public class Pacient {
    private @Id String CNP;
    private int id_user;
    private String last_name;
    private String first_name;
    private String email;
    private String phone;
    private Date born;
    private boolean is_active;
    private List<Appointment> appointments;

    public Pacient(String CNP, String last_name, String first_name, String email, String phone, Date born, boolean is_active, List<Appointment> appointments) {
        this.CNP = CNP;
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
        this.born = born;
        this.is_active = is_active;
        this.appointments = appointments;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
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