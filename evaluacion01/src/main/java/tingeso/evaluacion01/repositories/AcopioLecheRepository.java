package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.AcopioLecheEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;

import java.util.List;

@Repository
public interface AcopioLecheRepository extends JpaRepository<AcopioLecheEntity, String> {
    List<AcopioLecheEntity> findAllByProveedorAndQuincena(ProveedorEntity proveedor, QuincenaEntity quincena);
    boolean existsByQuincena(QuincenaEntity quincena);
}
