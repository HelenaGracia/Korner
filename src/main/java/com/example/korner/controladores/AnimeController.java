package com.example.korner.controladores;


import com.example.korner.modelo.Animes;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.servicio.AnimeServiceImpl;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/animes")
public class AnimeController {

    //En vez de autowired, inyectamos las dependencias mediante el constructor https://platzi.com/discusiones/2317-spring-boot/168849-que-diferencia-hay-entre-crear-el-constructor-y-utilizar-la-anotacion-autowired/
    private final AnimeServiceImpl animeService;
    private final GeneroElementoServiceImpl generoElementoService;

    private final Logger logger = LoggerFactory.getLogger(AnimeController.class);

    public AnimeController(AnimeServiceImpl animeService, GeneroElementoServiceImpl generoElementoService) {
        this.animeService = animeService;
        this.generoElementoService = generoElementoService;
    }

    @GetMapping
    public String showAnime(Model model) {
        List<Animes> listaAnime = animeService.getAll();
        model.addAttribute("anime", listaAnime);
        return "animes";
    }

//    @GetMapping
//    public String showAnime(Model model) {
//        List<Animes> listaAnime = animeService.getAll();
//        model.addAttribute("anime", listaAnime);
//        List<GeneroElementoCompartido> listaGenero = generoElementoService.getAll();
//        model.addAttribute("generoAnime", listaGenero);
//        return "animes";
//    }

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
