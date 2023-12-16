package medical.pdp.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "doctori")
public class Doctor {
    @Id
    @Column(name = "id_doctor")
    private Long idDoctor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "specializare")
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
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