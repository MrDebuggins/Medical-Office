package medical.gateway.DTOs;

import medical.gateway.entities.Specialization;

import java.util.Date;

public class PatientDTO
{
    public String login;
    public String password;
    public String cnp;
    public String lastname;
    public String firstname;
    public String email;
    public String phone;
    public Date born;
    public boolean active;
}
