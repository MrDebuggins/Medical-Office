package pdp.medical.pacients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.medical.DTOs.Pacient;

@Repository
public interface PacientsRepository extends JpaRepository<Pacient, String>
{
    Pacient findByCNP(String cnp);
}
