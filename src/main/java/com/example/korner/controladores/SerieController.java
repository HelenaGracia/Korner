package com.example.korner.controladores;

import com.example.korner.modelo.Serie;
import com.example.korner.servicio.SerieServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/series")
public class SerieController {

    private final SerieServiceImpl serieService;

    private final Logger logger = LoggerFactory.getLogger(SerieController.class);

    public SerieController(SerieServiceImpl serieService) {
        this.serieService = serieService;
    }

    @GetMapping("")
    public String listAllSeries(Model model){
        model.addAttribute("series", serieService.getAll());
        return "series";
    }

    @PostMapping("/save")
    public String save(Serie serie){
        logger.info("este es el objeto serie{}", serie);
        serieService.saveEntity(serie);
        return "redirect:/series";
    }

    @DeleteMapping("/delete")
    public String delete(Serie serie){
        serieService.deleteEntity(serie);
        return "redirect:/series";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteId(@PathVariable Integer id){
        serieService.deleteEntityById(id);
        return "redirect:/series";
    }
}
