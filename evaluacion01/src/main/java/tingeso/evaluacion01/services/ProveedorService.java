package tingeso.evaluacion01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.repositories.ProveedorRepository;

@Service
public class ProveedorService {
    @Autowired
    ProveedorRepository proveedor_repository;

    public void registrarProveedor(String codigo, String nombre, String categoria, String retencion){
        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setCodigo(codigo); // Verificar que tenga 5 digitos numericos
        proveedor.setNombre(nombre);
        proveedor.setCategoria(categoria); // Verificar que sea A, B, C o D
        proveedor.setRetencion(retencion); // Verificar que sea Si o No
        proveedor_repository.save(proveedor);
    }
}
