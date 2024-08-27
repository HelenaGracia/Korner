package com.example.korner.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accessDenied")
public class AccessDeniedController {
    /**
     * Este método proporciona una forma de manejar el acceso denegado redirigiendo al usuario a una página de inicio
     * para mostrar que no tienen acceso al recurso solicitado.
     * @return
     */
    @GetMapping
    public String showAccessDeniedPage() {
        return "redirect:/home" ;
    }
}
