package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.entities.DatosCentroAcopioEntity;
import tingeso.evaluacion01.entities.PagoEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.services.DatosCentroAcopioService;
import tingeso.evaluacion01.services.PagoService;

import java.util.List;

@Controller
@RequestMapping
public class PagoController {
    @Autowired
    PagoService pagoService;
    @Autowired
    DatosCentroAcopioService datosCentroAcopioService;

    @PostMapping("/planilla-pagos/calcular")
    public String calcularPlanillaPagos(@RequestParam("year") Integer year,
                                        @RequestParam("mes") Integer mes,
                                        @RequestParam("quincena") Integer numero,
                                        RedirectAttributes redirectAttr) {
        QuincenaEntity quincena = new QuincenaEntity(year, mes, numero);

        if(pagoService.existenPagosPorQuincena(quincena)){
            redirectAttr.addFlashAttribute("message",
                            "Estos pagos ya fueron calculados")
                    .addFlashAttribute("class", "informative-alert");
            List<PagoEntity> pagos = pagoService.obtenerPagosPorQuincena(quincena);
            redirectAttr.addFlashAttribute("pagos", pagos);
        }
        else if(datosCentroAcopioService.existenDatosCAParaCalculoPorQuincena(quincena)){
            try {
                List<DatosCentroAcopioEntity> listaDatosCa = datosCentroAcopioService.calcularDatosCAPorQuincena(quincena);
                datosCentroAcopioService.guardarListaDatosCA(listaDatosCa);
                List<PagoEntity> pagos = pagoService.calcularPagos(listaDatosCa);
                pagoService.guardarPagos(pagos);
                redirectAttr.addFlashAttribute("message", "Planilla de pagos calculada!")
                        .addFlashAttribute("class", "success-alert");
                redirectAttr.addFlashAttribute("pagos", pagos);
            } catch (Exception e) {
                redirectAttr.addFlashAttribute("message", e.getMessage())
                        .addFlashAttribute("class", "error-alert");
            }
        }
        else{
            redirectAttr.addFlashAttribute("message",
                            "No se han ingresado los datos del centro de acopio para el calculo de los pagos")
                    .addFlashAttribute("class", "error-alert");
        }

        return "redirect:/planilla-pagos/calcular";
    }

    @GetMapping("/planilla-pagos/calcular")
    public String calcularPlanillaPagosPage(){
        return "calcular_planilla_pagos";
    }

    @GetMapping("/planilla-pagos")
    public String planillaPagosPage(Model model){
        List<PagoEntity> pagos = pagoService.obtenerPagos();
        model.addAttribute("pagos", pagos);
        if(pagos.isEmpty()){
            model.addAttribute("message", "Aun no se han calculado pagos");
        }
        return "planilla_pagos";
    }
}
