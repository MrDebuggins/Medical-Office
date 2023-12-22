package medical.pdp.repositories;

import medical.pdp.entities.Doctor;
import medical.pdp.entities.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByIdDoctor(Long id_doctor);

    Page<Doctor> findBySpecialization(Specialization s, PageRequest pageable);

    Page<Doctor> findByLastnameContains(String like, PageRequest pageable);

    Page<Doctor> findBySpecializationAndLastnameContains(Specialization s, String like, PageRequest pageable);
}
