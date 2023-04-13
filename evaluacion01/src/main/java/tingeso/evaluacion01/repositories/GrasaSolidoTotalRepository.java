package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.GrasaSolidoTotalEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;

import java.util.Optional;

@Repository
public interface GrasaSolidoTotalRepository extends JpaRepository<GrasaSolidoTotalEntity, String> {
    Optional<GrasaSolidoTotalEntity> findByProveedorAndQuincena(ProveedorEntity proveedor, QuincenaEntity quincena);
    boolean existsByQuincena(QuincenaEntity quincena);
}
