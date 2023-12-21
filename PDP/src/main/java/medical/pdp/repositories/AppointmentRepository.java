package medical.pdp.repositories;

import medical.pdp.entities.Appointment;
import medical.pdp.entities.AppointmentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByIdPatient(String idPatient);

    Appointment findById(AppointmentKey id);

    List<Appointment> findById_DateBetween(Date dateStart, Date dateEnd);

    List<Appointment> findById_Date(Date date);

    void deleteById(AppointmentKey id);
}
