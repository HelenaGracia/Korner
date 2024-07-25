package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.servicio.PlataformaServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
        List<Plataforma> listadoPlataformas = plataformaService.getAll();
        Plataforma plataforma = new Plataforma();
        model.addAttribute("size", listadoPlataformas.size());
        model.addAttribute("plataformas", listadoPlataformas);
        model.addAttribute("datosPlataforma", plataforma);
        return "plataformas";
    }

    @PostMapping("/savePlataforma")
    public String savePlataforma(@Validated @ModelAttribute(name = "datosPlataforma") Plataforma plataforma,
                               BindingResult bindingResult, RedirectAttributes attributes){
        if (bindingResult.hasErrors()){
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "plataformas";

        }else {
            try {
                logger.info("este es el objeto plataforma recibido{}", plataforma);
                plataformaService.saveEntity(plataforma);
                logger.info("este es el objeto plataforma guardado{}", plataforma);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/plataformasElementos";
        }

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
