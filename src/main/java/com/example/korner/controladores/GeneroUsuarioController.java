package com.example.korner.controladores;

import com.example.korner.modelo.Genero;
import com.example.korner.servicio.GeneroUsuarioServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/generosUsuarios")
public class GeneroUsuarioController {


    private final GeneroUsuarioServiceImpl generoUsuarioService;

    public GeneroUsuarioController(GeneroUsuarioServiceImpl generoUsuarioService) {
        this.generoUsuarioService = generoUsuarioService;
    }


    @GetMapping("")
    public String generosUser(Model model){
        List<Genero> generos = generoUsuarioService.getAll();
        model.addAttribute("generosUsuario", generos);
        model.addAttribute("size", generos.size());
        model.addAttribute("newGenero", new Genero());
        return "generosUsuarios";
    }

    @PostMapping("/saveGeneroUser")
    public String saveGenero(@ModelAttribute("newGenero") Genero generoUser,
                             RedirectAttributes attributes){
        try {
            generoUsuarioService.saveEntity(generoUser);
            attributes.addFlashAttribute("success", "Elemento añadido correctamente");

        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }

        return "redirect:/generosUsuarios";
    }


    @PostMapping("/deleteGeneroUser")
    public String deleteGenero(Genero generoUser, RedirectAttributes attributes){
        try {
            generoUsuarioService.deleteEntity(generoUser);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/generosUsuarios";
    }

}
