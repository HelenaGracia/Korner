package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/gestion")
public class GestionController {
    private final UsuarioSecurityService usuarioSecurityService;

    public GestionController(UsuarioSecurityService usuarioSecurityService) {
        this.usuarioSecurityService = usuarioSecurityService;
    }

    @GetMapping("")
    public String showGestion(Model model, HttpSession session){
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("nombreUsuario",user.get().getNombre());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        return "gestion";
    }
}
