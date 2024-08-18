package com.example.korner.controladores;

import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestionUsuarios")
public class GestionUsuariosController {

    private final UsuarioSecurityService usuarioService;
    private  final AmigoServiceImpl amigoService;

    public GestionUsuariosController(UsuarioSecurityService usuarioService, AmigoServiceImpl amigoService) {
        this.usuarioService = usuarioService;
        this.amigoService = amigoService;
    }

    @GetMapping("")
    public String showUsuarios(Model model, HttpSession session){
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        List<Usuario> listaUsuarios = usuarioService.getAllUsuariosMenosEste(user.get().getId());
        model.addAttribute("usuarios", listaUsuarios);
        model.addAttribute("size", listaUsuarios.size());
        return "gestionUsuarios";
    }

    @GetMapping("/eliminarUsuario/{id}")
    public String deleteUser(@PathVariable Integer id){
        Optional<Usuario> usuarioEliminar = usuarioService.getById(id);
        usuarioService.deleteEntity(usuarioEliminar.get());
        return "redirect:/gestionUsuarios";
    }
}