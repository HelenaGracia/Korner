package com.example.korner.controladores;

import com.example.korner.modelo.FormatoLibro;
import com.example.korner.modelo.PlataformaVideojuego;
import com.example.korner.servicio.FormatoLibroServiceImpl;
import com.example.korner.servicio.PlataformaVideojuegoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/formatosLibros")
public class FormatoLibroController {


    private final FormatoLibroServiceImpl formatoLibroService;

    private final Logger logger = LoggerFactory.getLogger(FormatoLibroController.class);

    public FormatoLibroController( FormatoLibroServiceImpl formatoLibroService) {
        this.formatoLibroService = formatoLibroService;
    }

    @GetMapping("")
    public String showFormatosLibros(Model model, @RequestParam("page") Optional<Integer> page){

        paginacion(model,page);
        FormatoLibro formatoLibro = new FormatoLibro();
        model.addAttribute("datosFormato", formatoLibro);
        return "formatosLibros";
    }

    @PostMapping("/saveFormato")
    public String saveFormato(@Validated @ModelAttribute(name = "datosFormato") FormatoLibro formato,
                               BindingResult bindingResult, RedirectAttributes attributes,
                                 @RequestParam("page") Optional<Integer> page,
                                 Model model){
        paginacion(model,page);

        if (bindingResult.hasErrors()){
            model.addAttribute("formatoActual", -1);
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "formatosLibros";

        }else {
            try {
                logger.info("este es el objeto formato libro recibido{}", formato);
                formatoLibroService.saveEntity(formato);
                logger.info("este es el objeto formato libro guardado{}", formato);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al añadir el formato libro", e);
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al añadir el formato libro", e);
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/formatosLibros";
        }

    }


    @PostMapping("/saveFormatoModificar")
    public String saveFormatoModificar(@Validated @ModelAttribute(name = "datosFormato") FormatoLibro formato,
                                 BindingResult bindingResult, RedirectAttributes attributes,
                                          @RequestParam("page") Optional<Integer> page,
                                          Model model){
        paginacion(model, page);

        if (bindingResult.hasErrors()){
            model.addAttribute("formatoActual", formato.getId());
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "formatosLibros";

        }else {
            try {
                logger.info("este es el objeto formato libro recibido{}", formato);
                formatoLibroService.saveEntity(formato);
                logger.info("este es el objeto formato Libro guardado{}", formato);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al modificar el formato libro", e);
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al modificar el formato libro", e);
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/formatosLibros";
        }

    }


    @PostMapping("/deleteFormato")
    public String deleteFormato(FormatoLibro formato, RedirectAttributes attributes){
        try {
            logger.info("este es el objeto formato libro eliminado{}", formato);
            formatoLibroService.deleteEntity(formato);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
            logger.error("Error al eleminar el formato libro", e);
        }
        return "redirect:/formatosLibros";
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
        Page<FormatoLibro> pagina = formatoLibroService.findAll(pageRequest);

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

        model.addAttribute("formatos", pagina.getContent());



    }

}
