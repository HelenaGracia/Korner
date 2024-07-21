package com.example.korner.controladores;


import com.example.korner.modelo.Genero;
import com.example.korner.modelo.Rol;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.GeneroUsuarioServiceImpl;
import com.example.korner.servicio.RolServiceImpl;
import com.example.korner.servicio.UsuarioSecurityService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/creacion")
public class CuentaController {

    private final UsuarioSecurityService usuarioService;
    private final RolServiceImpl rolService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GeneroUsuarioServiceImpl generoUsuarioService;

    public CuentaController(UsuarioSecurityService usuarioService, RolServiceImpl rolService, BCryptPasswordEncoder bCryptPasswordEncoder, GeneroUsuarioServiceImpl generoUsuarioService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.generoUsuarioService = generoUsuarioService;
    }

    @GetMapping
    public String showCuenta(Model model) {

        //Inicio List Desplegable Año Nacimiento
        List<Integer> options = new ArrayList<>();
        int anyo = LocalDate.now().getYear();
        for (int i = anyo; i >= 1920; i--) {
            options.add(i);
        }
        //Fin List Desplegable Año Nacimiento

        List<Genero> listGenero = generoUsuarioService.getAll();
        model.addAttribute("generoUsuario", listGenero);
        model.addAttribute("options", options);
        return "cuenta" ;
    }


    @PostMapping("/save")
    public String save(Usuario usuario){
        Optional<Rol> role = rolService.getById(3);
        String password = bCryptPasswordEncoder.encode(usuario.getContrasena());
        if (role.isPresent()) {
            usuario.setRole(role.get());
            usuario.setContrasena(password);
            usuarioService.saveEntity(usuario);
        }
        return "redirect:/home";
    }
}
