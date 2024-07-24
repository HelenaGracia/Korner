package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Plataforma;
import com.example.korner.servicio.PlataformaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/plataformasElementos")
public class PlataformaController {


    private final PlataformaServiceImpl plataformaService;

    private final Logger logger = LoggerFactory.getLogger(PlataformaController.class);

    public PlataformaController(PlataformaServiceImpl plataformaService) {
        this.plataformaService = plataformaService;
    }

    @GetMapping("")
    public String showPlataformas(Model model){
        List<Plataforma> plataformas = plataformaService.getAll();
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("size", plataformas.size());
        return "plataformas";
    }

    @PostMapping("/savePlataforma")
    public String savePlataforma(@Validated  Plataforma plataforma,
                             BindingResult bindingResult, RedirectAttributes attributes){
        if (bindingResult.hasErrors()){
            attributes.addFlashAttribute("failed", "Error, el campo no puede estar en blanco");

        } else{
            try {

                logger.info("este es el objeto plataforma recibido{}", plataforma);

                plataformaService.saveEntity(plataforma);
                logger.info("este es el objeto plataforma guardado{}", plataforma);

                attributes.addFlashAttribute("success", "Elemento añadido correctamente");


            }catch (DataIntegrityViolationException e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            }

            catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error");
            }

        }

        return "redirect:/plataformasElementos";
    }


    @PostMapping("/deletePlataforma")
    public String deletePlataforma(Plataforma plataforma, RedirectAttributes attributes){
        try {
            logger.info("este es el objeto plataforma eliminado{}", plataforma);

            plataformaService.deleteEntity(plataforma);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/plataformasElementos";
    }

}
