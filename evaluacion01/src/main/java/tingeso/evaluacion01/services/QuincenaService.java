package tingeso.evaluacion01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.QuincenaRepository;

@Service
public class QuincenaService {
    @Autowired
    QuincenaRepository quincena_repository;

    public QuincenaEntity ingresarQuincena(Integer year, Integer mes, Integer numero){
        QuincenaEntity quincena = new QuincenaEntity();
        quincena.setYear(year);
        quincena.setMes(mes);
        quincena.setNumero(numero);
        String id = quincena.toString();
        quincena.setId(id);
        return quincena_repository.save(quincena);
    }

    public boolean estaRegistradaQuincena(Integer year, Integer mes, Integer numero) {
        String id = year.toString() + "/" + mes.toString() + "-" + numero.toString();
        return quincena_repository.findById(id).isPresent();
    }
}
