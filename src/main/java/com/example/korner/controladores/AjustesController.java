package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/ajustes")
public class AjustesController {

    private final UsuarioSecurityService usuarioService;

    public AjustesController(UsuarioSecurityService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String ajustes() {
        return "ajustes";
    }

    @PostMapping("/modificar")
    public String ajustesInicoSesion(@RequestParam(value = "ajustes",required = false) String ajustes, HttpSession session) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));

            user.get().setAjustes(ajustes);
            usuarioService.saveEntity(user.get());

        return "redirect:/ajustes";
    }
}
