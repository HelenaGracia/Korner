package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;



@Controller
@RequestMapping("/generosElementos")
public class GeneroElementoController {


    private final GeneroElementoServiceImpl generoElementoService;

    private final Logger logger = LoggerFactory.getLogger(GeneroElementoController.class);

    public GeneroElementoController(GeneroElementoServiceImpl generoElementoService) {
        this.generoElementoService = generoElementoService;
    }


    @GetMapping("")
    public String generos(Model model){
        List<GeneroElementoCompartido> generos = generoElementoService.getAll();
        model.addAttribute("generos", generos);
        model.addAttribute("size", generos.size());
        model.addAttribute("newGenero", new GeneroElementoCompartido());
        return "generosElementos";
    }

    @PostMapping("/saveGenero")
    public String saveGenero(@ModelAttribute("newGenero") GeneroElementoCompartido generoElementoCompartido,
                             RedirectAttributes attributes){
        try {

            logger.info("este es el objeto genero guardado{}", generoElementoCompartido);

            generoElementoService.saveEntity(generoElementoCompartido);
            attributes.addFlashAttribute("success", "Elemento añadido correctamente");


        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        }

        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }

        return "redirect:/generosElementos";
    }


    @PostMapping("/deleteGenero")
    public String deleteGenero(GeneroElementoCompartido generoElementoCompartido, RedirectAttributes attributes){
        try {
            logger.info("este es el objeto genero eliminado{}", generoElementoCompartido);

            generoElementoService.deleteEntity(generoElementoCompartido);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/generosElementos";
    }

}
