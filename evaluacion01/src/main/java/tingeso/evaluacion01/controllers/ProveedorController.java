package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.evaluacion01.services.ProveedorService;

@Controller
@RequestMapping
public class ProveedorController {
    @Autowired
    ProveedorService proveedor_service;

    @PostMapping("/proveedor/registrar")
    public String registrarProveedor(@RequestParam("codigo") String codigo,
                                     @RequestParam("nombre") String nombre,
                                     @RequestParam("categoria") String categoria,
                                     @RequestParam("retencion") String retencion,
                                     RedirectAttributes redirect_attr){
        proveedor_service.registrarProveedor(codigo, nombre, categoria, retencion);
        redirect_attr.addFlashAttribute("message", "Proveedor registrado correctamente!")
                     .addFlashAttribute("class", "success-alert");
        return "redirect:/proveedor/registrar";
    }

    @GetMapping("/proveedor/registrar")
    public String registrarProveedorPage(){
        return "registrar_proveedor";
    }
}
