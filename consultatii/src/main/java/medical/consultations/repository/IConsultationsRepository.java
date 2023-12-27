package medical.consultations.repository;

import medical.consultations.entities.Consultation;
import medical.consultations.entities.Investigation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface IConsultationsRepository extends MongoRepository<Consultation, String> {
    @Query("idDoctor:'?0'")
    List<Consultation> findByIdDoctor(@Param("idDoctor") Long idDoctor);

    @Query("idPatient:'?0'")
    List<Consultation> findByIdPatient(@Param("idPatient")String idPatient);

    Consultation findByIdPatientAndIdDoctorAndDate(String idPatient, Long idDoctor, Date date);
}
