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
import tingeso.evaluacion01.services.PagoService;
import tingeso.evaluacion01.services.QuincenaService;

import java.util.List;

@Controller
@RequestMapping
public class AcopioLecheController {
    @Autowired
    AcopioLecheService acopioLecheService;
    @Autowired
    QuincenaService quincenaService;
    @Autowired
    PagoService pagoService;

    @PostMapping("/acopios-leche/importar")
    public String importarAcopioLeche(@RequestParam("file")MultipartFile file,
                                      @RequestParam("year") Integer year,
                                      @RequestParam("mes") Integer mes,
                                      @RequestParam("quincena") Integer numero,
                                      RedirectAttributes redirectAttr) {
        QuincenaEntity quincena = quincenaService.ingresarQuincena(year, mes, numero);
        if(pagoService.existenPagosPorQuincena(quincena)){
            redirectAttr.addFlashAttribute("message", "Ya existen datos calculados para la quincena seleccionada")
                    .addFlashAttribute("class", "error-alert");
        }
        else{
            try {
                List<AcopioLecheEntity> acopiosLeche = acopioLecheService.leerExcel(file);
                acopioLecheService.validarListaAcopioLecheQuincena(acopiosLeche, quincena);
                acopioLecheService.guardarAcopiosLeches(acopiosLeche);
                redirectAttr.addFlashAttribute("message", "Datos registrados correctamente!")
                        .addFlashAttribute("class", "success-alert");
            } catch (Exception e) {
                redirectAttr.addFlashAttribute("message", e.getMessage())
                        .addFlashAttribute("class", "error-alert");
            }
        }

        return "redirect:/acopios-leche/importar";
    }

    @GetMapping("/acopios-leche/importar")
    public String importarAcopioLechePage(){
        return "subir_acopio_leche";
    }
}
