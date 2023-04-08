package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.entities.AcopioLecheEntity;
import tingeso.evaluacion01.entities.DatosCentroAcopioEntity;
import tingeso.evaluacion01.entities.PagoEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.services.DatosCentroAcopioService;
import tingeso.evaluacion01.services.PagoService;

import java.util.ArrayList;

@Controller
@RequestMapping
public class PagoController {
    @Autowired
    PagoService pago_service;
    @Autowired
    DatosCentroAcopioService datos_ca_service;

    @PostMapping("/planilla-pagos/calcular")
    public String calcularPlanillaPagos(@RequestParam("year") Integer year,
                                        @RequestParam("mes") Integer mes,
                                        @RequestParam("quincena") Integer numero,
                                        RedirectAttributes redirect_attr) {
        QuincenaEntity quincena = new QuincenaEntity(year, mes, numero);

        if(pago_service.existenPagosPorQuincena(quincena)){
            redirect_attr.addFlashAttribute("message",
                            "Estos pagos ya fueron calculados")
                    .addFlashAttribute("class", "informative-alert");
        }
        else if(datos_ca_service.existenDatosCAParaCalculoPorQuincena(quincena)){
            try {
                ArrayList<DatosCentroAcopioEntity> lista_datos_ca = datos_ca_service.calcularDatosCAPorQuincena(quincena);
                datos_ca_service.guardarListaDatosCA(lista_datos_ca);
                ArrayList<PagoEntity> pagos = pago_service.calcularPagos(lista_datos_ca);
                pago_service.guardarPagos(pagos);
                redirect_attr.addFlashAttribute("message", "Planilla de pagos calculada!")
                        .addFlashAttribute("class", "success-alert");
            } catch (Exception e) {
                redirect_attr.addFlashAttribute("message", e.getMessage())
                        .addFlashAttribute("class", "error-alert");
            }
        }
        else{
            redirect_attr.addFlashAttribute("message",
                            "No se han ingresado los datos del centro de acopio para el calculo de los pagos")
                    .addFlashAttribute("class", "error-alert");
        }

        return "redirect:/planilla-pagos/calcular";
    }

    @GetMapping("/planilla-pagos/calcular")
    public String calcularPlanillaPagosPage(){
        return "calcular_planilla_pagos";
    }
}
