package tingeso.evaluacion01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quincena")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuincenaEntity {
    @Id
    private String id;
    private Integer year;
    private Integer mes;
    private Integer numero;

    public String toString(){
        return year.toString() + "/" + mes.toString() + "-" + numero.toString();
    }
}
