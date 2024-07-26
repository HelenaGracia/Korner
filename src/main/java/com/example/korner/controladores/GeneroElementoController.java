package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String showGenerosElementos(Model model){
        List<GeneroElementoCompartido> listadoGeneros = generoElementoService.getAll();
        GeneroElementoCompartido genero = new GeneroElementoCompartido();
        model.addAttribute("size", listadoGeneros.size());
        model.addAttribute("generos", listadoGeneros);
        model.addAttribute("datosGenero", genero);
        return "generosElementos";
    }

    @PostMapping("/saveGeneroElemento")
    public String saveGeneroElemento(@Validated @ModelAttribute(name = "datosGenero") GeneroElementoCompartido generoElementoCompartido,
                                 BindingResult bindingResult, RedirectAttributes attributes){
        if (bindingResult.hasErrors()){
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "generosElementos";

        }else {
            try {
                logger.info("este es el objeto genero recibido{}", generoElementoCompartido);
                generoElementoService.saveEntity(generoElementoCompartido);
                logger.info("este es el objeto genero guardado{}", generoElementoCompartido);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/generosElementos";
        }

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
