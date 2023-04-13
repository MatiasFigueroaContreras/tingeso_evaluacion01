package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.services.ProveedorService;

import java.util.List;

@Controller
@RequestMapping
public class ProveedorController {
    @Autowired
    ProveedorService proveedorService;

    @PostMapping("/proveedores/registrar")
    public String registrarProveedor(@RequestParam("codigo") String codigo,
                                     @RequestParam("nombre") String nombre,
                                     @RequestParam("categoria") String categoria,
                                     @RequestParam("retencion") String retencion,
                                     RedirectAttributes redirectAttr){
        try {
            proveedorService.registrarProveedor(codigo, nombre, categoria, retencion);
            redirectAttr.addFlashAttribute("message", "Proveedor registrado correctamente!")
                    .addFlashAttribute("class", "success-alert");
        }
        catch (Exception e){
            redirectAttr.addFlashAttribute("message", e.getMessage())
                    .addFlashAttribute("class", "error-alert");
        }

        return "redirect:/proveedores/registrar";
    }

    @GetMapping("/proveedores/listar")
    public String listarProveedores(Model model){
        List<ProveedorEntity> proveedores = proveedorService.obtenerProveedores();
        model.addAttribute("proveedores", proveedores);
        if(proveedores.isEmpty()){
            model.addAttribute("message", "Aun no se han registrado proveedores");
        }
        return "listar_proveedores";
    }

    @GetMapping("/proveedores/registrar")
    public String registrarProveedorPage(){
        return "registrar_proveedor";
    }

}
