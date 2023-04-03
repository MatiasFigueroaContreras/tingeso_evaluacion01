package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.entities.AcopioLecheEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.services.AcopioLecheService;
import tingeso.evaluacion01.services.QuincenaService;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping
public class AcopioLecheController {
    @Autowired
    AcopioLecheService acopio_leche_service;
    @Autowired
    QuincenaService quincena_service;

    @PostMapping("/acopios-leche/importar")
    public String importarAcopioLeche(@RequestParam("file")MultipartFile file,
                                      @RequestParam("year") Integer year,
                                      @RequestParam("mes") Integer mes,
                                      @RequestParam("quincena") Integer numero,
                                      RedirectAttributes redirect_attr) {
        try {
            ArrayList<AcopioLecheEntity> acopios_leche = acopio_leche_service.leerExcel(file);
            QuincenaEntity quincena = quincena_service.ingresarQuincena(year, mes, numero);
            acopio_leche_service.validarDatosAcopioLecheQuincena(acopios_leche, quincena);
            acopio_leche_service.guardarAcopiosLecheQuincena(acopios_leche);
            redirect_attr.addFlashAttribute("message", "Datos registrados correctamente!")
                    .addFlashAttribute("class", "success-alert");
        } catch (Exception e) {
            redirect_attr.addFlashAttribute("message", e.getMessage())
                .addFlashAttribute("class", "error-alert");
        }

        return "redirect:/acopios-leche/importar";
    }

    @GetMapping("/acopios-leche/importar")
    public String importarAcopioLechePage(){
        return "subir_acopio_leche";
    }
}
