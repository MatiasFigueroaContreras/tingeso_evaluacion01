package tingeso.evaluacion01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.*;
import tingeso.evaluacion01.repositories.DatosCentroAcopioRepository;

import java.util.*;

@Service
public class DatosCentroAcopioService {
    @Autowired
    DatosCentroAcopioRepository datos_centro_acopio_repository;
    @Autowired
    AcopioLecheService acopio_leche_service;
    @Autowired
    GrasaSolidoTotalService grasa_solido_total_service;
    @Autowired
    ProveedorService proveedor_service;

    public void guardarDatosCA(DatosCentroAcopioEntity datos_centro_acopio){
        String id = datos_centro_acopio.getProveedor().getCodigo() + "-" + datos_centro_acopio.getQuincena().toString();
        datos_centro_acopio.setId(id);
        datos_centro_acopio_repository.save(datos_centro_acopio);
    }

    public void guardarListaDatosCA(ArrayList<DatosCentroAcopioEntity> lista_datos_ca){
        for(DatosCentroAcopioEntity datos_ca: lista_datos_ca){
            guardarDatosCA(datos_ca);
        }
    }

    public DatosCentroAcopioEntity obtenerDatosCAPorProveedorQuincena(ProveedorEntity proveedor, QuincenaEntity quincena) throws Exception{
        Optional<DatosCentroAcopioEntity> datos_ca = datos_centro_acopio_repository.findByProveedorAndQuincena(proveedor, quincena);
        if(!datos_ca.isPresent()){
            throw new Exception("No se encontraron los datos del centro de acopio, para el proveedor y quincena dados");
        }
        return datos_ca.get();
    }

    public boolean existenDatosCAParaCalculoPorQuincena(QuincenaEntity quincena){
        return  acopio_leche_service.existenAcopiosLechePorQuincena(quincena) &&
                grasa_solido_total_service.existeGrasaSolidoTotalPorQuincena(quincena);
    }

    public DatosCentroAcopioEntity calcularDatosCAPorProveedorQuincena(ProveedorEntity proveedor, QuincenaEntity quincena) throws Exception {
        DatosCentroAcopioEntity datos_centro_acopio = new DatosCentroAcopioEntity();
        datos_centro_acopio.setProveedor(proveedor);
        datos_centro_acopio.setQuincena(quincena);
        calcularDatosAcopioLeche(datos_centro_acopio);
        GrasaSolidoTotalEntity grasa_solido_total = grasa_solido_total_service.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena);
        datos_centro_acopio.setGrasa_solido_total(grasa_solido_total);
        calcularVariacionesDatosCA(datos_centro_acopio);
        return datos_centro_acopio;
    }

    public ArrayList<DatosCentroAcopioEntity> calcularDatosCAPorQuincena(QuincenaEntity quincena) throws Exception {
        ArrayList<ProveedorEntity> proveedores = proveedor_service.obtenerProveedores();
        ArrayList<DatosCentroAcopioEntity> lista_datos_ca = new ArrayList<>();
        for(ProveedorEntity proveedor: proveedores){
            DatosCentroAcopioEntity datos_ca_proveedor = calcularDatosCAPorProveedorQuincena(proveedor, quincena);
            lista_datos_ca.add(datos_ca_proveedor);
        }

        return lista_datos_ca;
    }

    public void calcularDatosAcopioLeche(DatosCentroAcopioEntity datos_centro_acopio){
        ArrayList<AcopioLecheEntity> acopios_leche = acopio_leche_service.obtenerAcopiosLechePorProveedorQuincena(datos_centro_acopio.getProveedor(), datos_centro_acopio.getQuincena());
        Integer n_dias_envio_m_t = 0;
        Integer n_dias_envio_m = 0;
        Integer n_dias_envio_t;
        Integer total_kls_leche = 0;
        HashMap<Integer, Boolean> dias_envios_m = new HashMap<>();
        HashMap<Integer, Boolean> dias_envios_t = new HashMap<>();
        for(AcopioLecheEntity acopio_leche : acopios_leche){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(acopio_leche.getFecha());
            Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
            String turno = acopio_leche.getTurno();
            if(turno.equals("M")){
                dias_envios_m.put(dia, Boolean.TRUE);
            }
            else {
                dias_envios_t.put(dia, Boolean.TRUE);
            }
            total_kls_leche += acopio_leche.getCantidad_leche();
        }

        for(Integer dia_envio_m: dias_envios_m.keySet()){
            if(dias_envios_t.containsKey(dia_envio_m)){
                n_dias_envio_m_t++;
            }
            else {
                n_dias_envio_m++;
            }
        }
        n_dias_envio_t = dias_envios_t.size() -  n_dias_envio_m_t;
        datos_centro_acopio.setDias_envio_m_t(n_dias_envio_m_t);
        datos_centro_acopio.setDias_envio_m(n_dias_envio_m);
        datos_centro_acopio.setDias_envio_t(n_dias_envio_t);
        datos_centro_acopio.setTotal_kls_leche(total_kls_leche);
    }

    public void calcularVariacionesDatosCA(DatosCentroAcopioEntity datos_centro_acopio) throws Exception{
        Integer variacion_leche;
        Integer variacion_grasa;
        Integer variacion_solido_total;
        GrasaSolidoTotalEntity grasa_st = datos_centro_acopio.getGrasa_solido_total();
        QuincenaEntity quincena_anterior = datos_centro_acopio.getQuincena().obtenerQuincenaAnterior();
        try {
            DatosCentroAcopioEntity datos_ca_anterior = obtenerDatosCAPorProveedorQuincena(datos_centro_acopio.getProveedor(), quincena_anterior);
            GrasaSolidoTotalEntity grasa_st_anterior = datos_ca_anterior.getGrasa_solido_total();
            variacion_leche = (datos_centro_acopio.getTotal_kls_leche() / datos_ca_anterior.getTotal_kls_leche() - 1) * 100;
            variacion_grasa = (grasa_st.getPorcentaje_grasa() / grasa_st_anterior.getPorcentaje_grasa() - 1) * 100;
            variacion_solido_total = (grasa_st.getPorcentaje_solido_total() / grasa_st_anterior.getPorcentaje_solido_total() - 1) * 100;
        }
        catch (Exception e){
            //No existen datos del centro acopio anteriormente.
            if(acopio_leche_service.existenAcopiosLechePorQuincena(quincena_anterior) ||
               grasa_solido_total_service.existeGrasaSolidoTotalPorQuincena(quincena_anterior)){
                throw new Exception("Existen pagos del centro de acopio no calculados para la quincena anterior");
            }
            else{
                variacion_leche = 0;
                variacion_grasa = 0;
                variacion_solido_total = 0;
            }
        }
        datos_centro_acopio.setVariacion_leche(variacion_leche);
        datos_centro_acopio.setVariacion_grasa(variacion_grasa);
        datos_centro_acopio.setVariacion_solido_total(variacion_solido_total);
    }
}
