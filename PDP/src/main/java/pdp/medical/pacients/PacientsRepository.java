package pdp.medical.pacients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacientsRepository extends JpaRepository<Pacient, String>
{
    Pacient findByCNP(String cnp);
}
