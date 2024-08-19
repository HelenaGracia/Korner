package com.example.korner.controladores;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String showHome(Model model, HttpSession session){
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        return "home";
    }

}
