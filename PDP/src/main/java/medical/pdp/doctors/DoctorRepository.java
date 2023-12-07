package pdp.medical.doctors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.medical.patients.Patient;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Doctor findByIdDoctor(Long id_doctor);
}
