package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.PagoEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, String> {
    boolean existsByQuincena(QuincenaEntity quincena);

    List<PagoEntity> findAllByQuincena(QuincenaEntity quincena);
    List<PagoEntity> findAllByOrderByQuincenaDescProveedorCodigoAsc();
}
