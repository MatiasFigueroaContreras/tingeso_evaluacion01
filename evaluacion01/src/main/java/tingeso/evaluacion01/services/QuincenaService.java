package tingeso.evaluacion01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.QuincenaRepository;

import java.time.LocalDateTime;


@Service
public class QuincenaService {
    @Autowired
    QuincenaRepository quincena_repository;

    public QuincenaEntity ingresarQuincena(Integer year, Integer mes, Integer numero) throws Exception{
        validarDatosQuincena(year, mes, numero);

        QuincenaEntity quincena = new QuincenaEntity();
        quincena.setYear(year);
        quincena.setMes(mes);
        quincena.setNumero(numero);
        String id = quincena.toString();
        quincena.setId(id);
        return quincena_repository.save(quincena);
    }

    public void validarDatosQuincena(Integer year, Integer mes, Integer numero) throws Exception{
        LocalDateTime fecha_actual = LocalDateTime.now();
        if(year < 0){
            throw new Exception("El año ingresado no es valido");
        }

        if(mes > 12 || mes < 1){
            throw new Exception("El mes de la quincena no es valido");
        }

        if(numero != 1 && numero != 2){
            throw new Exception("El numero de quincena no es valido");
        }

        //Verifica que la quincena este antes de la fecha actual con 2 dias de margen
        if(fecha_actual.isBefore(LocalDateTime.of(year, mes, 13*numero, 0, 0))){
            throw new Exception("La quincena ingresada es superior a la fecha actual");
        }
    }

    public boolean estaRegistradaQuincena(Integer year, Integer mes, Integer numero) {
        String id = year.toString() + "/" + mes.toString() + "-" + numero.toString();
        return quincena_repository.findById(id).isPresent();
    }
}
