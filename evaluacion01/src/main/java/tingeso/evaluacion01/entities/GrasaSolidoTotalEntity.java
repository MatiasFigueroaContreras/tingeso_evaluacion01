package tingeso.evaluacion01.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grasa_solido_total")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GrasaSolidoTotalEntity {
    @Id
    private String id;
    private Integer porcentaje_grasa;
    private Integer porcentaje_solido_total;
    @ManyToOne
    private ProveedorEntity proveedor;
    @ManyToOne
    private QuincenaEntity quincena;
}
