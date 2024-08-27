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

    /**
     * Este método es el encargado de mostrar la página de inicio de sesión a los usuarios.
     * @return @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/login")
    String login() {
        return "login";
    }

    /**
     *Este método se ejecuta después de un inicio de sesión exitoso y redirige al usuario a una página específica según sus preferencias
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * @return  redirección mediante una expresión switch, que selecciona la página adecuada o la página principal si no hay una preferencia específica.
     */
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
            case "gestion" -> "redirect:/gestion";
            default -> "redirect:/home";
        };

    }

    /**
     * Este método cierra la sesión del usuario eliminando los atributos de sesión que lo identifican,
     * como su ID, imagen de perfil y nombre. Después de limpiar la sesión, el usuario es redirigido a la página principal del sitio web
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return  redirige al usuario a la página principal del sitio web.
     */
    @GetMapping("/logout")
    String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        session.removeAttribute("imagenUsuario");
        session.removeAttribute("nameUsuario");
        return "redirect:";
    }

}
