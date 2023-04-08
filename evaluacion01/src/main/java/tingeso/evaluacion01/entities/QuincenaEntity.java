package tingeso.evaluacion01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public QuincenaEntity(Integer year, Integer mes, Integer numero){
        this.year = year;
        this.mes = mes;
        this.numero = numero;
        this.id = toString();
    }

    public String toString(){
        String mes_formateado = mes.toString();
        if(mes_formateado.length() == 1){
            mes_formateado = "0" + mes_formateado;
        }
        return year.toString() + "/" + mes_formateado + "/" + numero.toString();
    }

    public boolean estaDentroQuincena(Date fecha){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        int mes_fecha = calendar.get(Calendar.MONTH) + 1;
        int year_fecha = calendar.get(Calendar.YEAR);
        int dia_fecha = calendar.get(Calendar.DAY_OF_MONTH);
        if(numero == 1 && dia_fecha > 15){
            return false;
        } else if (numero == 2 && dia_fecha < 15) {
            return false;
        }
        return year_fecha == year && mes_fecha == mes;
    }

    public QuincenaEntity obtenerQuincenaAnterior(){
        QuincenaEntity quincena_anterior = new QuincenaEntity();
        if(numero == 2){
            quincena_anterior.setNumero(1);
            quincena_anterior.setMes(mes);
            quincena_anterior.setYear(year);
        }
        else if(mes == 1){
            quincena_anterior.setNumero(2);
            quincena_anterior.setMes(12);
            quincena_anterior.setYear(year - 1);
        }
        else {
            quincena_anterior.setNumero(2);
            quincena_anterior.setMes(mes - 1);
            quincena_anterior.setYear(year);
        }
        quincena_anterior.setId(quincena_anterior.toString());
        return quincena_anterior;
    }
}
