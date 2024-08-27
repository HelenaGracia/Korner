package com.example.korner.controladores;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Notificacion;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.NotificacionService;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
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
public class AmigosController {


    private final AmigoServiceImpl amigoService;
    private final UsuarioSecurityService usuarioService;
    private final NotificacionService notificacionService;

    public AmigosController(AmigoServiceImpl amigoService, UsuarioSecurityService usuarioService, NotificacionService notificacionService) {
        this.amigoService = amigoService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
    }

    /**
     * Metódo el cual obtiene una lista paginada de amigos y otra lista paginada de amigos bloqueados del usuario y pasa
     * la información a la vista asociada con el archivo html amigos
     * @param page número de página para la paginación de amigos
     * @param pagebloq número de página para la paginación de amigos bloqueados
     * @param session  permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */

    @GetMapping
    public String showAmigos(@RequestParam(value = "page",required = false) Optional<Integer> page,
                             @RequestParam(value = "pagebloq" , required = false) Optional<Integer> pagebloq,
                             HttpSession session,Model model) {

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 6);
        Page<Amigo> pagina = amigoService.getAllAmigos(user.get(), pageRequest);
        //Envio la pagina creada a la vista para poder verla
        model.addAttribute("pagina", pagina);
        //Obtengo la cantidad de paginas creadas, por ejemplo: 8
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
        model.addAttribute("amigo", pagina.getContent());

        //Paginación Bloqueados
        int currentPageBloq = pagebloq.orElse(1);
        Pageable pageRequestBloq = PageRequest.of(currentPageBloq - 1, 6);
        Page<Amigo> paginaBloq = amigoService.getAllAmigosBloqueados(user.get(), pageRequestBloq);
        model.addAttribute("paginaBloq", paginaBloq);
        int totalPagesBloq = paginaBloq.getTotalPages();
        if (totalPagesBloq > 0) {
            List<Integer> pageNumbersBloq = IntStream.rangeClosed(1, totalPagesBloq)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbersBloq", pageNumbersBloq);
        }
        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPageBloq", currentPageBloq);
        model.addAttribute("sizeBloq", paginaBloq.getContent().size());
        model.addAttribute("bloqueados", paginaBloq.getContent());


        return "amigos";
    }

    /**
     *Metódo el cual obtiene una lista paginada de solicitudes de amistad pendientes de un usuario y pasa
     *la información a la vista asociada con el archivo html amigosPendientes
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/solicitudesPendientes")
    public String solicitudesPendientes(@RequestParam("page") Optional<Integer> page,
                                        Model model, HttpSession session) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
        Page<Amigo> pagina = amigoService.getAllSolicitudesPendientes(user.get(),pageRequest);
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
        model.addAttribute("amigos", pagina.getContent());
        return "amigosPendientes";
    }

    /**
     * Método el cual maneja la aceptación de una solicitud de amistad. Primero obtiene los usuarios involucrados, actualiza
     * la relación de amistad para marcarla como aceptada, crea una nueva relación inversa, genera una notificación
     * para el usuario que envió la solicitud, y finalmente redirige al usuario a la página de solicitudes pendientes con un mensaje de éxito.
     * @param id numero del id del usuario que envió la solicitud de amistad (usuario origen)
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * que está aceptando la solicitud (usuario destino)
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso los mensaje de éxito o error
     * @return redirección al endpoint /amigos/solicitudesPendientes con mensaje
     */
    @GetMapping("/aceptarSolicitud/{id}")
    public String aceptarSolicitud(@PathVariable Integer id ,HttpSession session, RedirectAttributes attributes){
        Optional<Usuario> userDestino = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userOrigen= usuarioService.getById(id);
        Amigo amigoAceptado= amigoService.getAmigo(userDestino.get(),userOrigen.get());
        amigoAceptado.setPendiente(false);
        Amigo amigoNuevo = new Amigo();
        amigoNuevo.setBloqueado(false);
        amigoNuevo.setPendiente(false);
        amigoNuevo.setUsuarioOrigen(userDestino.get());
        amigoNuevo.setUsuarioDestino(userOrigen.get());
        amigoService.saveEntity(amigoAceptado);
        amigoService.saveEntity(amigoNuevo);
        Notificacion notificacion = new Notificacion();
        notificacion.setUserFrom(userDestino.get().getNombre());
        notificacion.setUserTo(userOrigen.get().getNombre());
        notificacion.setEstado("pendiente");
        notificacion.setMensaje("El usuario " + userDestino.get().getNombre() + " ha aceptado tu solicitud de amistad");
        notificacion.setTipoElemento("solicitud Aceptada");
        notificacion.setUserFromId(userDestino.get().getId());
        notificacion.setRutaImagenUserFrom(userDestino.get().getRutaImagen());
        notificacionService.saveEntity(notificacion);
        attributes.addFlashAttribute("success","La solicitud de amistad del usuario: " + userOrigen.get().getNombre() + " ha sido aceptada");
        return "redirect:/amigos/solicitudesPendientes";

    }

    /**
     * Método que maneja el rechazo de una solicitud de amistad. Primero obtiene al usuario actual y al usuario cuya
     * solicitud se va a rechazar. Obtiene y elimina la solcitud de amistad (Amigo amigoEliminar) y redirige al usuario
     * a la página donde se pueden ver las solicitudes pendientes de amistad.
     * @param id numero que representa el id de un usuario
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito
     * @return redirección al endpoint /amigos/solicitudesPendientes con mensaje
     */
    @GetMapping("/rechazarSolicitud/{id}")
    public String rechazarSolicitud(@PathVariable Integer id , HttpSession session, RedirectAttributes attributes){

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoEliminar = amigoService.getAmigo(user.get(),userDestino.get());
        amigoService.deleteEntity(amigoEliminar);
        attributes.addFlashAttribute("success","La solicitud de amistad del usuario: " + userDestino.get().getNombre() + " ha sido rechazada");

        return "redirect:/amigos/solicitudesPendientes";

    }

    /**
     *Metódo el cual obtiene una lista paginada de solicitudes de amistad enviadas de un usuario y pasa
     *la información a la vista asociada con el archivo html amigosSolicitudesEnviadas
     * @param page número de página para la paginación
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada
     */

    @GetMapping("/solicitudesEnviadas")
    public String verSolicitudesEnviadas(@RequestParam("page") Optional<Integer> page,
                                         Model model, HttpSession session){
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
        Page<Amigo> pagina = amigoService.getAllSolicitudesEnviadas(user.get(),pageRequest);
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
        model.addAttribute("amigos", pagina.getContent());

        return "amigosSolicitudesEnviadas";
    }

    /**
     * Método que maneja la eliminación de una solicitud de amistad enviada. Primero obtiene al usuario actual y al usuario al
     * cual se le envia la solicitud. Obtiene y elimina la solcitud de amistad (Amigo amigoEliminar,representa la relación de amistad)
     * y redirige al usuario a la página donde se pueden ver las solicitudes enviadas de amistad.
     * @param id numero que representa el id de un usuario
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de éxito
     * @return redirección al endpoint /amigos/solicitudesEnviadas con mensaje
     */

    @GetMapping("/eliminarSolicitud/{id}")
    public String eliminarSolicitudEnviada(@PathVariable Integer id , HttpSession session, RedirectAttributes attributes){

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoEliminar = amigoService.getAmigo(userDestino.get(),user.get());
        amigoService.deleteEntity(amigoEliminar);
        attributes.addFlashAttribute("success","La solicitud de amistad al usuario: " + userDestino.get().getNombre() + " ha sido eliminada");

        return "redirect:/amigos/solicitudesEnviadas";

    }

    /**
     * Este método se encarga de eliminar la relación de amistad entre dos usuarios (la directa y la inversa) eliminando
     * ambas entradas de la BBDD
     * @param id numero de id de la relacion de amistad (directa) que se desea eliminar
     * @return redirección al endpoint /amigos
     */
    @PostMapping("/delete")
    public String deleteAmigo(Integer id){
        Optional<Amigo> amigoOrigen = amigoService.getById(id);
        Amigo amigoDestino = amigoService.getAmigo(amigoOrigen.get().getUsuarioOrigen(),amigoOrigen.get().getUsuarioDestino());
        amigoService.deleteEntity(amigoOrigen.get());
        amigoService.deleteEntity(amigoDestino);
        return "redirect:/amigos";
    }

    /**
     * Este método se encarga de manejar el bloqueo de amigos en la aplicación. Cambia el estado del amigo (representación de
     * la relación de amistada directa) a bloqueado.
     * @param id numero del id del Objeto amigo que se desea bloquear
     * @return redirección al endpoint /amigos
     */

    @PostMapping("/bloquear")
    public String bloquearAmigo(Integer id){
        Optional<Amigo> amigoBloqueado = amigoService.getById(id);
        amigoBloqueado.get().setBloqueado(true);
        amigoService.saveEntity(amigoBloqueado.get());
        return "redirect:/amigos";
    }

    /**
     * Este método se encarga de manejar el desbloqueo de amigos en la aplicación. Cambia el estado del amigo (representación de
     * la relación de amistada directa) a desbloqueado.
     * @param id numero del id del Objeto amigo que se desea desbloquear
     * @return redirección al endpoint /amigos
     */
    @PostMapping("/desbloquear")
    public String desbloquearAmigo(Integer id){
        Optional<Amigo> amigoDesbloqueado = amigoService.getById(id);
        amigoDesbloqueado.get().setBloqueado(false);
        amigoService.saveEntity(amigoDesbloqueado.get());
        return "redirect:/amigos";
    }

}
