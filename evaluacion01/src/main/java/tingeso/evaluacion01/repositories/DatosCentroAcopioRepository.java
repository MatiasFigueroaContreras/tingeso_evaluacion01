package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.DatosCentroAcopioEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;

import java.util.Optional;

@Repository
public interface DatosCentroAcopioRepository extends JpaRepository<DatosCentroAcopioEntity, String> {
    Optional<DatosCentroAcopioEntity> findByProveedorAndQuincena(ProveedorEntity proveedor, QuincenaEntity quincena);
}
