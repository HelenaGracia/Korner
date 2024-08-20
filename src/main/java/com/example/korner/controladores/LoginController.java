package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class LoginController {

    private final UsuarioSecurityService usuarioService;

    public LoginController(UsuarioSecurityService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    String loginSuccess(HttpSession session) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        String ajustesUsuario = user.get().getAjustesInicioSesion();

        return switch (ajustesUsuario) {
            case "peliculas" -> "redirect:/peliculas";
            case "series" -> "redirect:/series";
            case "videojuegos" -> "redirect:/videojuegos";
            case "animes" -> "redirect:/animes";
            case "libros" -> "redirect:/libros";
            default -> "redirect:/home";
        };

    }

    @GetMapping("/logout")
    String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        session.removeAttribute("imagenUsuario");
        session.removeAttribute("nameUsuario");
        return "redirect:";
    }

}
