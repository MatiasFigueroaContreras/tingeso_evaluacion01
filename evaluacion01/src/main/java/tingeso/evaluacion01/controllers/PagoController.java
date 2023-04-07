package tingeso.evaluacion01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tingeso.evaluacion01.services.PagoService;

@Controller
@RequestMapping
public class PagoController {
    @Autowired
    PagoService pago_service;
}
