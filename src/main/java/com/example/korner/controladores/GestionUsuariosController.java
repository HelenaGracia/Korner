package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/gestionUsuarios")
public class GestionUsuariosController {

    private final UsuarioSecurityService usuarioService;

    public GestionUsuariosController(UsuarioSecurityService usuarioService) {
        this.usuarioService = usuarioService;

    }
    private final Logger logger = LoggerFactory.getLogger(GestionUsuariosController.class);

    @GetMapping("")
    public String showUsuarios(@RequestParam("page") Optional<Integer> page, Model model, HttpSession session){

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage-1, 10);

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Page<Usuario> pagina = usuarioService.getAllUsuariosMenosEste(user.get().getId(),pageRequest);
        //Envio la pagina creada a la vista para poder verla
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("usuarios", pagina.getContent());
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "gestionUsuarios";
    }

    @GetMapping("/eliminarUsuario/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes attributes){
        Optional<Usuario> usuarioEliminar = usuarioService.getById(id);
        usuarioService.deleteEntity(usuarioEliminar.get());
        attributes.addFlashAttribute("success","El usuario: " + usuarioEliminar.get().getNombre() + " ha sido eliminado");
        return "redirect:/gestionUsuarios";
    }

    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstadoUser(@PathVariable Integer id, RedirectAttributes attributes){
        Optional<Usuario> usuarioInactivo = usuarioService.getById(id);
        if(usuarioInactivo.get().getActiva()){
            usuarioInactivo.get().setActiva(false);
            attributes.addFlashAttribute("success","El usuario: " + usuarioInactivo.get().getNombre() + " ahora esta inactivo");
            usuarioService.saveEntity(usuarioInactivo.get());
            return "redirect:/gestionUsuarios";
        }else usuarioInactivo.get().setActiva(true);
        attributes.addFlashAttribute("success","El usuario: " + usuarioInactivo.get().getNombre() + " ahora esta activo");
        usuarioService.saveEntity(usuarioInactivo.get());
        return "redirect:/gestionUsuarios";
    }

    /**
     * Metódo en el que se realiza una búsqueda de un usuario a través de su nombre o el email
     * @param busqueda String recibido desde el input del formulario
     * @param model Model
     * @param session HttpSession
     * @param attributes RedirectAttributes
     * @return archivo html gestionUsuarios o redirect al endpoint /gestionUsuarios
     */
    @GetMapping("/search")
    public String search(@RequestParam(name = "busqueda", required = false) String busqueda,
                         Model model,
                         HttpSession session, RedirectAttributes attributes){
        try {
            model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
            model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

            Optional<Usuario> userbyName = usuarioService.getByName(busqueda);
            Optional<Usuario> userByEmail = usuarioService.getByCorreo(busqueda);

            if (userbyName.isPresent()){
                model.addAttribute("usuarioBusqueda", userbyName.get());

            }else {
                if (userByEmail.isPresent()){
                    model.addAttribute("usuarioBusqueda", userByEmail.get());

                }
                else {
                    attributes.addFlashAttribute("failed", "La búsqueda por correo o usuario no ha dado resultado" +
                            " puede que el correo o usuario sea incorrecto o que el usuario esté eliminado");
                    return "redirect:/gestionUsuarios";
                }
            }

        }catch (Exception e){
            logger.error("Error en la busqueda por correo o nombre de usuario");
            model.addAttribute("failed", "Error en la búsqueda");
            return "redirect:/gestionUsuarios";
        }
        return "gestionUsuarios";
    }
}