package pdp.medical.DTOs;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doctori")
public class Doctor {
    @Id
    @Column(name = "id_doctor")
    private String id_doctor;

    @Column(name = "id_user")
    private int id_user;

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
}