package com.example.korner.controladores;

import com.example.korner.modelo.FormatoLibro;
import com.example.korner.modelo.PlataformaVideojuego;
import com.example.korner.servicio.FormatoLibroServiceImpl;
import com.example.korner.servicio.PlataformaVideojuegoServiceImpl;
import jakarta.servlet.http.HttpSession;
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

    /**
     * Este método es responsable de preparar los datos necesarios para la página que muestra una lista de formatos de libros,
     * gestiona la paginación y proporciona al modelo de la vista un objeto vacío de tipo FormatoLibro. La vista
     * renderiza estos datos para permitir al usuario ver  la lista de formatos de libros
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("")
    public String showFormatosLibros(Model model, @RequestParam("page") Optional<Integer> page, HttpSession session){

        paginacion(model,page, session);
        FormatoLibro formatoLibro = new FormatoLibro();
        model.addAttribute("datosFormato", formatoLibro);
        return "formatosLibros";
    }

    /**
     * Este método se encarga de la creacion de un formato de libro. Recibe de un formulario los datos, valida esos datos
     * y guardar toda esta información en la base de datos. En caso de errores gestiona esos errores mostrando mensajes
     * informativos al usuario y evita guardar datos incorrectos.
     * @param formato recibe y valida el objeto FormatoLibro que se llena con los datos del formulario.
     * @param bindingResult contiene los resultados de la validación, incluyendo posibles errores
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada o redirección al endpoint /formatosLibros
     */
    @PostMapping("/saveFormato")
    public String saveFormato(@Validated @ModelAttribute(name = "datosFormato") FormatoLibro formato,
                               BindingResult bindingResult, RedirectAttributes attributes,
                                 @RequestParam("page") Optional<Integer> page,
                                 Model model, HttpSession session){
        paginacion(model,page, session);

        if (bindingResult.hasErrors()){
            model.addAttribute("formatoActual", -1);
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "formatosLibros";

        }else {
            try {
                formatoLibroService.saveEntity(formato);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al añadir el formato libro por nombres duplicados", e);
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al añadir el formato libro", e);
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/formatosLibros";
        }

    }

    /**
     * Este método se encarga de la modificación de un formato de libro. Recibe de un formulario los datos a modificar, valida esos datos
     * y guardar toda esta información en la base de datos. En caso de errores gestiona esos errores mostrando mensajes
     * informativos al usuario y evita guardar datos incorrectos.
     * @param formato recibe y valida el objeto FormatoLibro que se llena con los datos del formulario.
     * @param bindingResult contiene los resultados de la validación, incluyendo posibles errores
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada o redirección al endpoint /formatosLibros
     */
    @PostMapping("/saveFormatoModificar")
    public String saveFormatoModificar(@Validated @ModelAttribute(name = "datosFormato") FormatoLibro formato,
                                 BindingResult bindingResult, RedirectAttributes attributes,
                                          @RequestParam("page") Optional<Integer> page,
                                          Model model, HttpSession session){
        paginacion(model, page, session);

        if (bindingResult.hasErrors()){
            model.addAttribute("formatoActual", formato.getId());
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "formatosLibros";

        }else {
            try {
                formatoLibroService.saveEntity(formato);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al modificar el formato libro por nombres duplicados", e);
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al modificar el formato libro", e);
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/formatosLibros";
        }

    }

    /**
     * Este método se encarga de eliminar un formato de libro específico de la BBDD
     * @param id Recibe el parámetro id desde el formulario o la solicitud. Este parámetro corresponde al identificador
     * de la FormatoLibro que se desea eliminar
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @return se redirige al usuario a la vista de formatosLibros (/formatosLibros), mostrando el mensaje correspondiente
     * (de éxito o de error) en función de cómo haya transcurrido el proceso.
     */
    @PostMapping("/deleteFormato")
    public String deleteFormato(Integer id, RedirectAttributes attributes){
        try {
            formatoLibroService.deleteEntityById(id);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
            logger.error("Error al eleminar el formato libro", e);
        }
        return "redirect:/formatosLibros";
    }

    /**
     * Este método se encarga de gestionar la paginación de la lista de formatos de libros
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     */
    private void paginacion(Model model, Optional<Integer> page, HttpSession session){
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
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

    }

}
