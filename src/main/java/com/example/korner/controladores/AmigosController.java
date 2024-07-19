package com.example.korner.controladores;


import com.example.korner.modelo.Amigo;
import com.example.korner.servicio.AmigoServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/amigos")
public class AmigosController {


    private final AmigoServiceImpl amigoService;

    public AmigosController(AmigoServiceImpl amigoService) {
        this.amigoService = amigoService;
    }


    @GetMapping
    public String showAmigos(Model model) {
        List<Amigo> listaAmigos = amigoService.getAll();
        model.addAttribute("amigo", listaAmigos);
        return "amigos";
    }

    @PostMapping("/save")
    public String save(Amigo amigos){
        amigoService.saveEntity(amigos);
        return "redirect:/amigos";
    }

    @PostMapping("/delete")
    public String delete(Amigo amigos){
        amigoService.deleteEntity(amigos);
        return "redirect:/amigos";
    }


    @PostMapping("/delete/{id}")
    public String deleteId(@PathVariable Integer id){
        amigoService.deleteEntityById(id);
        return "redirect:/amigos";
    }



}
