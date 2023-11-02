package pdp.medical.DTOs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;
import java.util.List;

@Entity
public class Doctor {
    private @Id String id_doctor;
    private int id_user;
    private String last_name;
    private String first_name;
    private String email;
    private String phone;
    private Specialization specialization;
    private List<Appointment> appointments;
}