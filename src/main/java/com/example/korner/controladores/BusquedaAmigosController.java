package com.example.korner.controladores;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Notificacion;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.NotificacionService;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/amigos")
public class BusquedaAmigosController {


    private final AmigoServiceImpl amigoService;
    private final UsuarioSecurityService usuarioService;
    private final NotificacionService notificacionService;

    public BusquedaAmigosController(AmigoServiceImpl amigoService, UsuarioSecurityService usuarioService, NotificacionService notificacionService) {
        this.amigoService = amigoService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
    }
    private final Logger logger = LoggerFactory.getLogger(BusquedaAmigosController.class);

    /**
     * Este método se encarga de buscar usuarios en la aplicación excluyendo al usuario que hace uso de la aplicación
     * y a los usuarios que son amigos de este.  Los resultados se muestran paginados y, si no se encuentran coincidencias
     * o hay un error, se muestran mensajes de error correspondientes.
     * @param nombreUser cadena que contiene el nombre del usuario que se desea buscar, recibido desde el formulario
     * @param page número de página para la paginación
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada dónde se mostrarán los resultados de la búsqueda
     */
    @GetMapping("/search")
    public String search(@RequestParam(value = "amigosBusqueda",required = false) String nombreUser,
                         @RequestParam(value = "page") Optional<Integer> page,HttpSession session, Model model) {
        try{
            Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
            model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
            model.addAttribute("nameUsuario",session.getAttribute("userName").toString());            //lista Amigos del usuario sin distincion de bloqueados o pendientes
            List<Amigo> listAmigos = amigoService.getAllAmigosList(user.get());
            //lista de Id de esos amigos
            List<Integer> listIdAmigos= new java.util.ArrayList<>(listAmigos.stream().map(Amigo::getUsuarioDestino).map(Usuario::getId).toList());
            //lista de id con amigos y el usuario actual
            listIdAmigos.add(user.get().getId());

            int currentPage = page.orElse(1);
            Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
            Page<Usuario> pagina = null;
            if(nombreUser == null || nombreUser.isEmpty()){
                model.addAttribute("busquedaFallida","Debe introducir un nombre de usuario");
                return "busquedaAmigos";
            }else {
                pagina = usuarioService.getAllUsuariosSinListIdSinInactivos(nombreUser,listIdAmigos,pageRequest);
                model.addAttribute("busquedaUsuarios",nombreUser);
                if(pagina.getContent().isEmpty() ){
                    model.addAttribute("busquedaFallida","No se ha encontrado ningun usuario con ese nombre");

                }

                model.addAttribute("pagina", pagina);
                int totalPages = pagina.getTotalPages();
                if (totalPages > 0) {
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                            .boxed()
                            .collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }
                model.addAttribute("currentPage", currentPage);
                model.addAttribute("size", pagina.getContent().size());
                model.addAttribute("usuarios", pagina.getContent());

            }

        } catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");

        }

        return "busquedaAmigos";
    }

    /**
     *Este método permite a un usuario enviar una solicitud de amistad a otro usuario en la aplicación. A
     * demás de crear la solicitud en la base de datos, también se envía una notificación al destinatario de la
     * solicitud informándole sobre la misma. Finalmente, se redirige al usuario a la página de búsqueda de amigos,
     * mostrando un mensaje de éxito si la solicitud se envió correctamente.
     * @param id  identificador del usuario al que se enviará la solicitud
     * @param nombreUser nombre del usuario que se está buscando
     * @param session permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito
     * @return  redirige al usuario de vuelta a la página de búsqueda de amigos, con el nombre del usuario que se
     * estaba buscando incluido en la URL como parámetro.
     */
    @GetMapping("/enviarSolicitud/{id}/{nombreUser}")
    public String enviarSolicitud(@PathVariable Integer id ,
                                  @PathVariable String nombreUser,
                                  HttpSession session, RedirectAttributes attributes){
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoOrigen = new Amigo();
        amigoOrigen.setPendiente(true);
        amigoOrigen.setUsuarioOrigen(user.get());
        amigoOrigen.setUsuarioDestino(userDestino.get());
        amigoOrigen.setBloqueado(false);
        amigoService.saveEntity(amigoOrigen);
        Notificacion notificacion = new Notificacion();
        notificacion.setUserFrom(user.get().getNombre());
        notificacion.setUserTo(userDestino.get().getNombre());
        notificacion.setEstado("pendiente");
        notificacion.setMensaje("El usuario " + user.get().getNombre() + " te ha enviado una solcitud de amistad");
        notificacion.setTipoElemento("solicitud Enviada");
        notificacion.setUserFromId(user.get().getId());
        notificacion.setIdTipoElemento(amigoOrigen.getId());
        notificacion.setRutaImagenUserFrom(user.get().getRutaImagen());
        notificacionService.saveEntity(notificacion);
        attributes.addFlashAttribute("success","La solicitud ha sido enviada al usuario: "+userDestino.get().getNombre());
        return "redirect:/amigos/search?amigosBusqueda="+nombreUser;

    }




}
