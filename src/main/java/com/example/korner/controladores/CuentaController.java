package com.example.korner.controladores;


import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/creacion")
public class CuentaController {

    private final UsuarioSecurityService usuarioService;

    public CuentaController(UsuarioSecurityService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String showCuenta() {
        return "cuenta" ;
    }

    @PostMapping("/save")
    public String save(Usuario usuario){

        usuarioService.saveEntity(usuario);
        return "redirect:/home";
    }
}
