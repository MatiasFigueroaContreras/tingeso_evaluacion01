package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.PagoEntity;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
}
