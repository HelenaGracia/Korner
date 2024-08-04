package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/generosElementos")
public class GeneroElementoController {


    private final GeneroElementoServiceImpl generoElementoService;

    private final Logger logger = LoggerFactory.getLogger(GeneroElementoController.class);

    public GeneroElementoController(GeneroElementoServiceImpl generoElementoService) {
        this.generoElementoService = generoElementoService;
    }


    @GetMapping("")
    public String showGenerosElementos(Model model, @RequestParam("page") Optional<Integer> page){

        paginacion(model,page);

        //List<GeneroElementoCompartido> listadoGeneros = generoElementoService.getAll();
        GeneroElementoCompartido genero = new GeneroElementoCompartido();
        //model.addAttribute("size", listadoGeneros.size());
        //model.addAttribute("generos", listadoGeneros);
        model.addAttribute("datosGenero", genero);
        return "generosElementos";
    }

    @PostMapping("/saveGeneroElemento")
    public String saveGeneroElemento(@Validated @ModelAttribute(name = "datosGenero") GeneroElementoCompartido generoElementoCompartido,
                                 BindingResult bindingResult, RedirectAttributes attributes,
                                     @RequestParam("page") Optional<Integer> page,
                                     Model model){
        paginacion(model,page);

        if (bindingResult.hasErrors()){
            model.addAttribute("generoElementoActual", -1);
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

    @PostMapping("/saveGeneroElementoModificar")
    public String saveGeneroElementoModificar(@Validated @ModelAttribute(name = "datosGenero") GeneroElementoCompartido generoElementoCompartido,
                                     BindingResult bindingResult, RedirectAttributes attributes,
                                     @RequestParam("page") Optional<Integer> page,
                                     Model model){
        paginacion(model,page);

        if (bindingResult.hasErrors()){
            model.addAttribute("generoElementoActual", generoElementoCompartido.getId());
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

    private void paginacion(Model model, Optional<Integer> page){
        //Recibe la pagina en la que estoy si no recibe nada asigna la pagina 1
        int currentPage = page.orElse(1);
        //Guarda la pagina en la que estoy (Si es la pagina 1, la 2...) y la cantidad de elementos que quiero mostrar en ella
        PageRequest pageRequest = PageRequest.of(currentPage-1, 10);
        /*
         se crea un objeto page que es el encargado de rellenar en la pagina que le has indicado con la cantidad
         que le has dicho todos los objetos pelicula almacenados, es decir, crea la pagina que visualizas con el contenido
         */
        // Page<Pelicula> pagina = peliculaService.findAll(pageRequest);
        Page<GeneroElementoCompartido> pagina = generoElementoService.findAll(pageRequest);

        //Envio la pagina creada a la vista para poder verla
        model.addAttribute("pagina", pagina);
        //Obtengo la cantidad de paginas creadas, por ejemplo: 8
        int totalPages = pagina.getTotalPages();

        /*
         Si la cantidad total de paginas es superior a 0 obtiene una lista con los numeros de pagina, es decir
         si tengo un total de 8 paginas va a crear una lista de Integer almacenando los valores 1,2,3,4,5,6,7,8
         de esta forma obtego todos los numeros de pagina y los envio a la vista
         */
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPage", currentPage);

        //getContent() returns just that single page's data

        model.addAttribute("size", pagina.getContent().size());

        model.addAttribute("generos", pagina.getContent());

    }

}
