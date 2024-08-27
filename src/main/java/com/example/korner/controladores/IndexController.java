package com.example.korner.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    /**
     * Este método es el encargado de mostrar la página de inicio de la aplicaión.
     * @return @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("")
    public String showIndex(){
        return "index";
    }
}
