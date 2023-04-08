package tingeso.evaluacion01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.evaluacion01.entities.DatosCentroAcopioEntity;
import tingeso.evaluacion01.entities.PagoEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.PagoRepository;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class PagoService {
    @Autowired
    PagoRepository pago_repository;
    HashMap<String, Integer> PAGO_POR_KLS_LECHE = new HashMap<>() {{
        put("A", 700);
        put("B", 550);
        put("C", 400);
        put("D", 250);
    }};
    Double RETENCION = 0.13;
    Integer PAGA_RETENCION = 950000;

    public boolean existenPagosPorQuincena(QuincenaEntity quincena){
        return pago_repository.existsByQuincena(quincena);
    }

    public void guardarPago(PagoEntity pago){
        pago.setId(pago.getProveedor().getCodigo() + "-" + pago.getQuincena().toString());
        pago_repository.save(pago);
    }

    public void guardarPagos(ArrayList<PagoEntity> pagos){
        for(PagoEntity pago: pagos){
            guardarPago(pago);
        }
    }

    public PagoEntity calcularPago(DatosCentroAcopioEntity datos_centro_acopio){
        PagoEntity pago = new PagoEntity();
        pago.setProveedor(datos_centro_acopio.getProveedor());
        pago.setQuincena(datos_centro_acopio.getQuincena());
        pago.setDatos_centro_acopio(datos_centro_acopio);
        pago.setPago_leche(calcularPagoLeche(datos_centro_acopio));
        pago.setPago_grasa(calcularPagoGrasa(datos_centro_acopio));
        pago.setPago_solido_total(calcularPagoSolidoTotal(datos_centro_acopio));
        pago.setBonificacion_frecuencia(calcularBonificacionPorFrecuencia(datos_centro_acopio, pago.getPago_leche()));
        Integer pago_acopio_leche = pago.getPagoAcopioLeche();
        pago.setDcto_variacion_leche(calcularDctoVariacionLeche(datos_centro_acopio, pago_acopio_leche));
        pago.setDcto_variacion_grasa(calcularDctoVariacionGrasa(datos_centro_acopio, pago_acopio_leche));
        pago.setDcto_variacion_solido_total(calcularDctoVariacionSolidoTotal(datos_centro_acopio, pago_acopio_leche));
        pago.setPago_total(pago.getPagoAcopioLeche() - pago.getDescuentos());
        pago.setMonto_retencion(calcularMontoRetencion(pago));
        pago.setMonto_final(pago.getPago_total() - pago.getMonto_retencion());
        return pago;
    }

    public ArrayList<PagoEntity> calcularPagos(ArrayList<DatosCentroAcopioEntity> lista_datos_ca){
        ArrayList<PagoEntity> pagos = new ArrayList<>();
        for(DatosCentroAcopioEntity datos_ca: lista_datos_ca){
            PagoEntity pago = calcularPago(datos_ca);
            pagos.add(pago);
        }

        return pagos;
    }

    private Integer calcularPagoLeche(DatosCentroAcopioEntity datos_centro_acopio){
        String categoria_proveedor = datos_centro_acopio.getProveedor().getCategoria();
        Integer total_kls_leche = datos_centro_acopio.getTotal_kls_leche();
        return PAGO_POR_KLS_LECHE.get(categoria_proveedor) * total_kls_leche;
    }

    private Integer calcularPagoGrasa(DatosCentroAcopioEntity datos_centro_acopio){
        Integer porcentaje_grasa = datos_centro_acopio.getGrasa_solido_total().getPorcentaje_grasa();
        Integer total_kls_leche = datos_centro_acopio.getTotal_kls_leche();
        Integer pago;
        if(porcentaje_grasa >= 0 && porcentaje_grasa <= 20){
            pago = 30 * total_kls_leche;
        }
        else if(porcentaje_grasa >= 21 && porcentaje_grasa <= 45){
            pago = 80 * total_kls_leche;
        }
        else {
            pago = 120 * total_kls_leche;
        }

        return pago;
    }

    private Integer calcularPagoSolidoTotal(DatosCentroAcopioEntity datos_centro_acopio){
        Integer porcentaje_solido_total = datos_centro_acopio.getGrasa_solido_total().getPorcentaje_solido_total();
        Integer total_kls_leche = datos_centro_acopio.getTotal_kls_leche();
        Integer pago;
        if(porcentaje_solido_total >= 0 && porcentaje_solido_total <= 7){
            pago = -130 * total_kls_leche;
        }
        else if(porcentaje_solido_total >= 8 && porcentaje_solido_total <= 18){
            pago = -90 * total_kls_leche;
        }
        else if(porcentaje_solido_total >= 19 && porcentaje_solido_total <= 35) {
            pago = 95 * total_kls_leche;
        }
        else {
            pago = 150 * total_kls_leche;
        }

        return pago;
    }

    private Integer calcularBonificacionPorFrecuencia(DatosCentroAcopioEntity datos_centro_acopio, Integer pago_leche){
        Integer pago = 0;
        if(datos_centro_acopio.getDias_envio_m_t() > 10){
            pago = (int) Math.floor(0.20 * pago_leche);
        }
        else if (datos_centro_acopio.getDias_envio_m() > 10){
            pago = (int) Math.floor(0.12 * pago_leche);
        }
        else if (datos_centro_acopio.getDias_envio_t() > 10){
            pago = (int) Math.floor(0.08 * pago_leche);
        }

        return pago;
    }

    private Integer calcularDctoVariacionLeche(DatosCentroAcopioEntity datos_centro_acopio, Integer pago_acopio_leche){
        Integer variacion_negativa_leche = datos_centro_acopio.getVariacion_leche() * -1;
        Integer descuento = 0;

        if(variacion_negativa_leche >= 9 && variacion_negativa_leche <= 25){
            descuento = (int) Math.floor(0.07 * pago_acopio_leche);
        }
        else if(variacion_negativa_leche >= 26 && variacion_negativa_leche <= 45){
            descuento = (int) Math.floor(0.15 * pago_acopio_leche);
        }
        else if(variacion_negativa_leche >= 46) {
            descuento = (int) Math.floor(0.30 * pago_acopio_leche);
        }

        return descuento;
    }

    private Integer calcularDctoVariacionGrasa(DatosCentroAcopioEntity datos_centro_acopio, Integer pago_acopio_leche){
        Integer variacion_negativa_grasa = datos_centro_acopio.getVariacion_grasa() * -1;
        Integer descuento = 0;

        if(variacion_negativa_grasa >= 16 && variacion_negativa_grasa <= 25){
            descuento = (int) Math.floor(0.12 * pago_acopio_leche);
        }
        else if(variacion_negativa_grasa >= 26 && variacion_negativa_grasa <= 40){
            descuento = (int) Math.floor(0.20 * pago_acopio_leche);
        }
        else if(variacion_negativa_grasa >= 41) {
            descuento = (int) Math.floor(0.30 * pago_acopio_leche);
        }

        return descuento;
    }

    private Integer calcularDctoVariacionSolidoTotal(DatosCentroAcopioEntity datos_centro_acopio, Integer pago_acopio_leche){
        Integer variacion_negativa_solido_total = datos_centro_acopio.getVariacion_solido_total() * -1;
        Integer descuento = 0;

        if(variacion_negativa_solido_total >= 7 && variacion_negativa_solido_total <= 12){
            descuento = (int) Math.floor(0.18 * pago_acopio_leche);
        }
        else if(variacion_negativa_solido_total >= 13 && variacion_negativa_solido_total <= 35){
            descuento = (int) Math.floor(0.27 * pago_acopio_leche);
        }
        else if(variacion_negativa_solido_total >= 36) {
            descuento = (int) Math.floor(0.45 * pago_acopio_leche);
        }

        return descuento;
    }

    private Integer calcularMontoRetencion(PagoEntity pago){
        Integer pago_total = pago.getPago_total();
        Integer monto_retencion = 0;
        ProveedorEntity proveedor = pago.getProveedor();
        if(pago_total > PAGA_RETENCION && proveedor.estaAfectoARetencion()){
            monto_retencion = (int) Math.floor(RETENCION * pago_total);
        }

        return monto_retencion;
    }
}
