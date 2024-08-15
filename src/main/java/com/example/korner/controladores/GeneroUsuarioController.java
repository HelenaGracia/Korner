package com.example.korner.controladores;

import com.example.korner.modelo.Genero;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.servicio.GeneroUsuarioServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/generosUsuarios")
public class GeneroUsuarioController {


    private final GeneroUsuarioServiceImpl generoUsuarioService;

    public GeneroUsuarioController(GeneroUsuarioServiceImpl generoUsuarioService) {
        this.generoUsuarioService = generoUsuarioService;
    }


    @GetMapping("")
    public String generosUser(@RequestParam("page") Optional<Integer> page, Model model){

        paginacion(model,page);
        model.addAttribute("newGenero", new Genero());
        return "generosUsuarios";
    }

    @PostMapping("/saveGeneroUser")
    public String saveGenero(@Validated @ModelAttribute("newGenero") Genero generoUser,
                             BindingResult bindingResult,
                             @RequestParam("page") Optional<Integer> page,
                             RedirectAttributes attributes,Model model){
        paginacion(model,page);
        if (bindingResult.hasErrors()) {
            model.addAttribute("generoUsuarioActual", -1);
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "generosUsuarios";
        }else {
            try {
                generoUsuarioService.saveEntity(generoUser);
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

        return "redirect:/generosUsuarios";
    }

    @PostMapping("/modificarGeneroUser")
    public String modificarGenero(@Validated @ModelAttribute("newGenero") Genero generoUser,
                             BindingResult bindingResult,
                             @RequestParam("page") Optional<Integer> page,
                             RedirectAttributes attributes,Model model){
        paginacion(model,page);
        if (bindingResult.hasErrors()) {
            model.addAttribute("generoUsuarioActual", generoUser.getId());
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "generosUsuarios";
        }else {
            try {
                generoUsuarioService.saveEntity(generoUser);
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

        return "redirect:/generosUsuarios";
    }


    @PostMapping("/deleteGeneroUser")
    public String deleteGenero(Genero generoUser, RedirectAttributes attributes){
        try {
            generoUsuarioService.deleteEntity(generoUser);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/generosUsuarios";
    }

    private void paginacion(Model model, Optional<Integer> page){

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage-1, 10);
        Page<Genero> pagina = generoUsuarioService.findAll(pageRequest);

        //Envio la pagina creada a la vista para poder verla
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("generosUsuario", pagina.getContent());

    }
}
