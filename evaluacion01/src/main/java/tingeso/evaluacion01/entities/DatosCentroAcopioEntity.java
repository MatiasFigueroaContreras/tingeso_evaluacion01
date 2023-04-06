package tingeso.evaluacion01.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="datos_centro_acopio")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DatosCentroAcopioEntity {
    @Id
    @SequenceGenerator(name = "centro_acopio_sequence", sequenceName = "centro_acopio_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centro_acopio_sequence")
    private Long id;
    private Integer dias_envio_m_t;
    private Integer dias_envio_m;
    private Integer dias_envio_t;
    private Integer total_kls_leche;
    private Integer variacion_leche;
    private Integer variacion_grasa;
    private Integer variacion_solido_total;
    @OneToOne
    private GrasaSolidoTotalEntity grasa_solido_total;
    @ManyToOne
    private ProveedorEntity proveedor;
    @ManyToOne
    private QuincenaEntity quincena;
}
