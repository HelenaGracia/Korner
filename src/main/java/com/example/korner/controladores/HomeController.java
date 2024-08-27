package com.example.korner.controladores;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    /**
     *Este método se encarga de obtener los datos de la sesión del usuario, enviarlos a la vista y devolver la
     * vista renderizada de la plantilla home.html con los datos proporcionados
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session  permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     *la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("")
    public String showHome(Model model, HttpSession session){
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "home";
    }

}
