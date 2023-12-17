package medical.pdp.entities;

import jakarta.persistence.*;

import java.util.Date;


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

    @Temporal(TemporalType.DATE)
    @Column(name = "nascut")
    private Date born;

    @Column(name = "activ")
    private boolean isActive = true;

/*    @MapsId("id")
    @OneToMany
    private Set<Appointment> appointments;*/

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

/*    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }*/
}