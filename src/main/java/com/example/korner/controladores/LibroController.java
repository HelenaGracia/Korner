package com.example.korner.controladores;

import com.example.korner.modelo.Animes;
import com.example.korner.modelo.Libro;
import com.example.korner.servicio.LibroServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {

    //En vez de autowired, inyectamos las dependencias mediante el constructor
    private final LibroServiceImpl libroService;
    private final Logger logger = LoggerFactory.getLogger(LibroController.class);

    public LibroController(LibroServiceImpl libroService) {
        this.libroService = libroService;
    }

    
    @GetMapping
    public String showLibros(Model model){
        List<Libro> listaLibros = libroService.getAll();
        model.addAttribute("libro", listaLibros);
        return "libros";
    }


    @PostMapping("/save")
    public String save(Libro libro, RedirectAttributes redirectAttributes){
        logger.info("este es el objeto libro{}", libro);
        libroService.saveEntity(libro);
        redirectAttributes.addFlashAttribute("mensaje", "Libro añadido");
        return "redirect:/libros";
    }

    @PostMapping("/delete")
    public String delete(Libro libro){
        libroService.deleteEntity(libro);
        return "redirect:/libros";
    }

}
