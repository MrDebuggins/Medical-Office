package medical.consultations;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IConsultationsRepository extends MongoRepository<Consultation, String> {
    @Query("idDoctor:'?0'")
    List<Consultation> findByIdDoctor(Long idDoctor);

    @Query("idPatient:'?0'")
    List<Consultation> findByIdPatient(Long idPatient);

    public long count();
}
