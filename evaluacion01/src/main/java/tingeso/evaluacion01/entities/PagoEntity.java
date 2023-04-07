package tingeso.evaluacion01.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pago")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PagoEntity {
    @Id
    @SequenceGenerator(name = "pago_sequence", sequenceName = "pago_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pago_sequence")
    private Long id;
    private Integer pago_leche;
    private Integer pago_grasa;
    private Integer pago_solido_total;
    private Integer bonificacion_frecuencia;
    private Integer dcto_variacion_leche;
    private Integer dcto_variacion_grasa;
    private Integer dcto_variacion_solido_total;
    private Integer pago_total;
    private Integer monto_retencion;
    private Integer monto_final;
    @ManyToOne
    private ProveedorEntity proveedor;
    @ManyToOne
    private QuincenaEntity quincena;
    @ManyToOne
    private DatosCentroAcopioEntity datos_centro_acopio;

    public Integer getPagoAcopioLeche(){
        return pago_leche + pago_grasa + pago_solido_total + bonificacion_frecuencia;
    }

    public Integer getDescuentos(){
        return dcto_variacion_grasa + dcto_variacion_leche + dcto_variacion_solido_total;
    }
}
