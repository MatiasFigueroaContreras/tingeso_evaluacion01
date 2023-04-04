package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.entities.GrasaSolidoTotalEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.services.GrasaSolidoTotalService;
import tingeso.evaluacion01.services.QuincenaService;

import java.util.ArrayList;

@Controller
@RequestMapping
public class GrasaSolidoTotalController {
    @Autowired
    GrasaSolidoTotalService grasa_solido_total_service;
    @Autowired
    QuincenaService quincena_service;

    @PostMapping("/grasas-solidos-totales/importar")
    public String importarAcopioLeche(@RequestParam("file") MultipartFile file,
                                      @RequestParam("year") Integer year,
                                      @RequestParam("mes") Integer mes,
                                      @RequestParam("quincena") Integer numero,
                                      RedirectAttributes redirect_attr) {
        try {
            ArrayList<GrasaSolidoTotalEntity> grasas_solidos_totales = grasa_solido_total_service.leerExcel(file);
            QuincenaEntity quincena = quincena_service.ingresarQuincena(year, mes, numero);
            grasa_solido_total_service.guardarGrasasSolidosTotales(grasas_solidos_totales, quincena);
            redirect_attr.addFlashAttribute("message", "Datos registrados correctamente!")
                    .addFlashAttribute("class", "success-alert");
        } catch (Exception e) {
            redirect_attr.addFlashAttribute("message", e.getMessage())
                    .addFlashAttribute("class", "error-alert");
        }

        return "redirect:/grasas-solidos-totales/importar";
    }

    @GetMapping("/grasas-solidos-totales/importar")
    public String importarAcopioLechePage(){
        return "subir_datos_grasas_solidos";
    }
}
