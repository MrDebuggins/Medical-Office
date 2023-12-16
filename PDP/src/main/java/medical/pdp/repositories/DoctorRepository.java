package medical.pdp.repositories;

import medical.pdp.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Doctor findByIdDoctor(Long id_doctor);
}
