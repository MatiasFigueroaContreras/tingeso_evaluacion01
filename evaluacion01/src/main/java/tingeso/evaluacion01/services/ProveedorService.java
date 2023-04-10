package tingeso.evaluacion01.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.repositories.ProveedorRepository;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ProveedorService {
    @Autowired
    ProveedorRepository proveedor_repository;
    private String CATEGORIAS_VALIDAS[] = {"A", "B", "C", "D"};
    public void registrarProveedor(String codigo, String nombre, String categoria, String retencion) throws  Exception{
        validarDatosProveedor(codigo, categoria, retencion);
        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setCodigo(codigo);
        proveedor.setNombre(nombre);
        proveedor.setCategoria(categoria);
        proveedor.setRetencion(retencion);
        proveedor_repository.save(proveedor);
    }

    public void validarDatosProveedor(String codigo, String categoria, String retencion) throws Exception {
        //Verificacion de un codigo correcto (5 digitos numericos)
        if(codigo.length() != 5){
            throw new Exception("El codigo tiene que ser de 5 digitos numericos");
        }
        try {
            Integer codigo_int = Integer.parseInt(codigo);
            if(codigo_int < 0){
                throw new Exception("El codigo tiene que ser de 5 digitos numericos");
            }
        }
        catch (NumberFormatException e) {
            throw new Exception("El codigo tiene que ser de 5 digitos numericos");
        }
        //Verificacion de categorias validas establecidas
        if(!Arrays.asList(CATEGORIAS_VALIDAS).contains(categoria)){
            throw new Exception("La categoria ingresada no es valida");
        }
        //Verificacion de valores validos para retencion
        if(!retencion.equals("Si") && !retencion.equals("No")){
            throw new Exception("El afecto a retencion ingresado no es valido");
        }

        if(proveedor_repository.findById(codigo).isPresent()){
            throw new Exception("El proveedor ya se encuentra registrado");
        }
    }

    public ArrayList<ProveedorEntity> obtenerProveedores(){
        return new ArrayList<ProveedorEntity>(proveedor_repository.findAll());
    }

    public boolean existeProveedor(ProveedorEntity proveedor){
        return proveedor_repository.existsById(proveedor.getCodigo());
    }
}
