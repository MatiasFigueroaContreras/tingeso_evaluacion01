package tingeso.evaluacion01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tingeso.evaluacion01.entities.GrasaSolidoTotalEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;

import java.util.List;

@Repository
public interface GrasaSolidoTotalRepository extends JpaRepository<GrasaSolidoTotalEntity, String> {
    public GrasaSolidoTotalEntity findByProveedorAndQuincena(ProveedorEntity proveedor, QuincenaEntity quincena);
}
