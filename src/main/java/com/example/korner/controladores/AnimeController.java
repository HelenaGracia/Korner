package com.example.korner.controladores;


import com.example.korner.servicio.AnimeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/animes")
public class AnimeController {

    @Autowired
    private AnimeServiceImpl animeService;

    private final Logger logger = LoggerFactory.getLogger(AnimeController.class);

    @GetMapping
    public String showAnime(Model model) {
        List<Animes> listaAnime = animeService.getAll();
        model.addAttribute("anime", listaAnime);
        return "animes";
    }

    @PostMapping("/save")
    public String save(Animes anime){
        logger.info("este es el objeto anime{}", anime);
        animeService.saveEntity(anime);
        return "redirect:/animes";
    }


    @PostMapping("/delete")
    public String delete(Animes anime){
        animeService.deleteEntity(anime);
        return "redirect:/animes";
    }


    @PostMapping("/delete/{id}")
    public String deleteId(@PathVariable Integer id){
        animeService.deleteEntityById(id);
        return "redirect:/animes";
    }

}
