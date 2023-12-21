package medical.pdp.repositories;

import medical.pdp.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String>
{
    Patient findByIdUser(Long id_user);

    Patient findByCNP(String cnp);

    @Query(value = "update Medical.pacienti set activ = 0 where cnp = ?1", nativeQuery = true)
    void setInactive(String id);
}
