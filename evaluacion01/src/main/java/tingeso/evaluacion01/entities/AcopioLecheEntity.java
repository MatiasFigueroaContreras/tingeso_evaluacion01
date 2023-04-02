package tingeso.evaluacion01.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "acopio_leche")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcopioLecheEntity {
    @Id
    @SequenceGenerator(name = "acopio_leche_sequence", sequenceName = "acopio_leche_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acopio_leche_sequence")
    private Long id;
    private String turno;
    private Integer cantidad_leche;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date fecha;
    @ManyToOne
    private ProveedorEntity proveedor;
    @ManyToOne
    private QuincenaEntity quincena;
}