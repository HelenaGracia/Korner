package com.example.korner.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestion")
public class GestionController {
    @GetMapping("")
    public String showGestion(){
        return "gestion";
    }
}
