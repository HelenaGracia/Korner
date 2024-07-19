package com.example.korner.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/creacion")
public class CuentaController {

    @GetMapping
    public String showCuenta() {
        return "cuenta" ;
    }
}
