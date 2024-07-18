package com.example.korner.controladores;

import com.example.korner.modelo.Pelicula;

import com.example.korner.servicio.PeliculaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {


    private final PeliculaServiceImpl peliculaService;

    private final Logger logger = LoggerFactory.getLogger(PeliculaController.class);

    public PeliculaController(PeliculaServiceImpl peliculaService) {
        this.peliculaService = peliculaService;
    }


    @GetMapping("")
    public String listAllPeliculas(Model model){
        model.addAttribute("peliculas", peliculaService.getAll());
        return "peliculas";
    }



    @PostMapping("/save")
    public String save(Pelicula pelicula){
        logger.info("este es el objeto pelicula{}", pelicula);
        peliculaService.saveEntity(pelicula);
        return "redirect:/peliculas";
    }

    @DeleteMapping("/delete")
    public String delete(Pelicula pelicula){
        peliculaService.deleteEntity(pelicula);
        return "redirect:/peliculas";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteId(@PathVariable Integer id){
        peliculaService.deleteEntityById(id);
        return "redirect:/peliculas";
    }
}
