package com.example.korner.controladores;

import com.example.korner.modelo.Genero;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.servicio.GeneroUsuarioServiceImpl;
import jakarta.servlet.http.HttpSession;
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

    /**
     * Este método es responsable de preparar los datos necesarios para la página que muestra una lista de géneros de las personas.
     * Gestiona la paginación y proporciona al modelo de la vista un objeto vacío de tipo Genero.
     * La vista renderiza estos datos para permitir al usuario ver la lista de géneros
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return  String del nombre de la vista que debe ser renderizada
     */

    @GetMapping("")
    public String generosUser(@RequestParam("page") Optional<Integer> page, Model model, HttpSession session){

        paginacion(model,page, session);
        model.addAttribute("newGenero", new Genero());
        return "generosUsuarios";
    }

    /**
     * Este método se encarga de la creacion de un género de las personas. Recibe de un formulario los datos, valida esos datos
     * y guardar toda esta información en la base de datos. En caso de errores gestiona esos errores mostrando mensajes
     * informativos al usuario y evita guardar datos incorrectos.
     * @param generoUser recibe y valida el objeto Genero que se llena con los datos del formulario.
     * @param bindingResult contiene los resultados de la validación, incluyendo posibles errores
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada o redirección al endpoint /generosUsuarios
     */
    @PostMapping("/saveGeneroUser")
    public String saveGenero(@Validated @ModelAttribute("newGenero") Genero generoUser,
                             BindingResult bindingResult,
                             @RequestParam("page") Optional<Integer> page,
                             RedirectAttributes attributes,Model model, HttpSession session){
        paginacion(model,page, session);
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

    /**
     * Este método se encarga de la modificación de un género de las personas. Recibe de un formulario los datos a modificar, valida esos datos
     * y guardar toda esta información en la base de datos. En caso de errores gestiona esos errores mostrando mensajes
     * informativos al usuario y evita guardar datos incorrectos.
     * @param generoUser recibe y valida el objeto Genero que se llena con los datos del formulario.
     * @param bindingResult contiene los resultados de la validación, incluyendo posibles errores
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada o redirección al endpoint /generosUsuarios
     */
    @PostMapping("/modificarGeneroUser")
    public String modificarGenero(@Validated @ModelAttribute("newGenero") Genero generoUser,
                             BindingResult bindingResult,
                             @RequestParam("page") Optional<Integer> page,
                             RedirectAttributes attributes,Model model, HttpSession session){
        paginacion(model,page, session);
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

    /**
     * Este método se encarga de eliminar un género de personas específico de la BBDD
     * @param id Recibe el parámetro id desde el formulario o la solicitud. Este parámetro corresponde al identificador
     * del Genero que se desea eliminar
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito o error
     * @return se redirige al usuario a la vista de generosUsuarios (/generosUsuarios), mostrando el mensaje correspondiente
     * (de éxito o de error) en función de cómo haya transcurrido el proceso.
     */
    @PostMapping("/deleteGeneroUser")
    public String deleteGenero(Integer id, RedirectAttributes attributes){
        try {
            generoUsuarioService.deleteEntityById(id);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/generosUsuarios";
    }

    /**
     * Este método se encarga de gestionar la paginación de la lista de géneros de las personas
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     */

    private void paginacion(Model model, Optional<Integer> page, HttpSession session){

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
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
    }
}
