package medical.consultations.repository;

import medical.consultations.entities.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IConsultationsRepository extends MongoRepository<Consultation, String> {
    @Query("idDoctor:'?0'")
    List<Consultation> findByIdDoctor(@Param("idDoctor") Long idDoctor);

    @Query("idPatient:'?0'")
    List<Consultation> findByIdPatient(@Param("idPatient")Long idPatient);
}
