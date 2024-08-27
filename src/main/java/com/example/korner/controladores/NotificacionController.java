package com.example.korner.controladores;

import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NotificacionController {
    private final NotificacionService notificacionService;
    private final PeliculaServiceImpl peliculaService;
    private final SerieServiceImpl serieService;
    private final LibroServiceImpl libroService;
    private final AnimeServiceImpl animeService;
    private final VideojuegoServiceImpl videojuegoService;
    private final UsuarioSecurityService usuarioSecurityService;

    private final AmigoServiceImpl amigoService;

    public NotificacionController(NotificacionService notificacionService, PeliculaServiceImpl peliculaService, SerieServiceImpl serieService, LibroServiceImpl libroService, AnimeServiceImpl animeService, VideojuegoServiceImpl videojuegoService, UsuarioSecurityService usuarioSecurityService, AmigoServiceImpl amigoService) {
        this.notificacionService = notificacionService;
        this.peliculaService = peliculaService;
        this.serieService = serieService;
        this.libroService = libroService;
        this.animeService = animeService;
        this.videojuegoService = videojuegoService;
        this.usuarioSecurityService = usuarioSecurityService;
        this.amigoService = amigoService;
    }

    /**
     * Este método se encarga de contar el número de notificaciones pendientes que tiene un usuario. Comprueba que
     * las notificaiones estén actualizadas y sean relevantes antes de devolver un recuento. Actualiza las notificaciones
     * basándose en la existencia del contenido y el estado de los usuarios relacionados, eliminando notificaciones obsoletas o
     * no contando las que proceden de usuarios con la cuenta inactiva
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @return respuesta HTTP (ResponseEntity), donde el cuerpo de la respuesta es un string que representa el número de notificaciones
     */
    @GetMapping("/numeroNotificaciones")
    public ResponseEntity<String> contarNotificacionesPendientes(HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"pendiente");
            for(Notificacion notificacion: listaNotificaciones){
                switch (notificacion.getTipoElemento()){
                    case "pelicula":
                        Optional<Pelicula> pelicula = peliculaService.getById(notificacion.getIdTipoElemento());
                        if (pelicula.isEmpty()){
                            notificacionService.deleteEntity(notificacion);

                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isPresent()){
                            if (!usuarioSecurityService.getByName(notificacion.getUserFrom()).get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);

                            }
                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                    case "serie":
                        Optional<Serie> serie = serieService.getById(notificacion.getIdTipoElemento());
                        if (serie.isEmpty()){
                            notificacionService.deleteEntity(notificacion);

                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isPresent()){
                            if (!usuarioSecurityService.getByName(notificacion.getUserFrom()).get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);

                            }
                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                    case "libro":
                        Optional<Libro> libro = libroService.getById(notificacion.getIdTipoElemento());
                        if (libro.isEmpty()){
                            notificacionService.deleteEntity(notificacion);

                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isPresent()){
                            if (!usuarioSecurityService.getByName(notificacion.getUserFrom()).get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);

                            }
                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                    case "videojuego":
                        Optional<Videojuego> videojuego = videojuegoService.getById(notificacion.getIdTipoElemento());
                        if (videojuego.isEmpty()){
                            notificacionService.deleteEntity(notificacion);

                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isPresent()){
                            if (!usuarioSecurityService.getByName(notificacion.getUserFrom()).get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);

                            }
                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                    case "anime":
                        Optional<Anime> anime = animeService.getById(notificacion.getIdTipoElemento());
                        if (anime.isEmpty()){
                            notificacionService.deleteEntity(notificacion);

                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isPresent()){
                            if (!usuarioSecurityService.getByName(notificacion.getUserFrom()).get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);

                            }
                        }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                    case "solicitud Aceptada":
                        Optional<Usuario> usuario = usuarioSecurityService.getById(notificacion.getUserFromId());
                        if (usuario.isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }else {
                            if (!usuario.get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);
                            }
                        }break;
                    case "solicitud Enviada":
                        Optional<Usuario> user = usuarioSecurityService.getById(notificacion.getUserFromId());
                        if (user.isEmpty()){
                            notificacionService.deleteEntity(notificacion);
                        }else if(amigoService.getById(notificacion.getIdTipoElemento()).isPresent()){
                            if (!user.get().getActiva()){
                                notificacion.setEstadoUsuario("inactivo");
                                notificacionService.saveEntity(notificacion);
                            }
                            else {
                                notificacion.setEstadoUsuario("activo");
                                notificacion.setRutaImagenUserFrom(usuarioSecurityService.getById(notificacion.getUserFromId()).get().getRutaImagen());
                                notificacionService.saveEntity(notificacion);
                            }

                        }else {
                            notificacionService.deleteEntity(notificacion);
                        }
                        break;
                }
            }
        List<Notificacion> listaNotificacionesActualizada = notificacionService.getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(nombreUsuario,"pendiente", "activo");


        return ResponseEntity.ok(String.valueOf(listaNotificacionesActualizada.size())) ;
        }


    /**
     * Este método se encarga de mostrar en la vista las notificaciones de un usuario. Actualiza las notificaciones,
     * marcándolas como léidas y basándose en la existencia del contenido y el estado de los usuarios relacionados,
     * eliminando notificaciones obsoletas o no mostrando las que proceden de usuarios con la cuenta inactiva
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación de notificaciones
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/leerNotificaciones")
    public String leerNotificaciones(Model model, @RequestParam("page") Optional<Integer> page, HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(nombreUsuario,"pendiente", "activo");
        listaNotificaciones.forEach(notificacion -> {
            notificacion.setEstado("leido");
            notificacionService.saveEntity(notificacion);
        });
        List<Notificacion> listaNotificacionesLeidas = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"leido");
        for (Notificacion notificacion : listaNotificacionesLeidas){
            Optional<Usuario> usuario = usuarioSecurityService.getById(notificacion.getUserFromId());
            if (usuario.isEmpty()){
                notificacionService.deleteEntity(notificacion);
            }else {
                if (usuario.get().getActiva()){
                    notificacion.setEstadoUsuario("activo");
                    notificacion.setRutaImagenUserFrom(usuario.get().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    switch (notificacion.getTipoElemento()){
                        case "pelicula":
                            Optional<Pelicula> pelicula = peliculaService.getById(notificacion.getIdTipoElemento());
                            if (pelicula.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                        case "serie":
                            Optional<Serie> serie = serieService.getById(notificacion.getIdTipoElemento());
                            if (serie.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                        case "anime":
                            Optional<Anime> anime = animeService.getById(notificacion.getIdTipoElemento());
                            if (anime.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                        case "libro":
                            Optional<Libro> libro = libroService.getById(notificacion.getIdTipoElemento());
                            if (libro.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                        case "videojuego":
                            Optional<Videojuego> videojuego = videojuegoService.getById(notificacion.getIdTipoElemento());
                            if (videojuego.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                        case "solicitud Enviada":
                            Optional<Amigo> amigo = amigoService.getById(notificacion.getIdTipoElemento());
                            if (amigo.isEmpty()){
                                notificacionService.deleteEntity(notificacion);
                            }
                            break;
                    }

                }else{
                    notificacion.setEstadoUsuario("inactivo");
                    notificacionService.saveEntity(notificacion);
                }
            }
        }

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);;
        Page<Notificacion> pagina = notificacionService.getAllNotificacionesByUserAndEstadoEstadoUsuarioPage(nombreUsuario, "leido","activo", pageRequest);
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
        model.addAttribute("notificaciones", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",nombreUsuario);

        return "notificaciones";
    }

    /**
     * Este método se encarga de eliminar de la BBDD las notificaciones del usuario que esten en estado de leido
     * @param session permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de exíto
     * @return redirección al endpoint /leerNotificaciones
     */

    @GetMapping("/eliminarNotificaciones")
    public String eliminarNotificaciones(HttpSession session, RedirectAttributes attributes){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"leido");
        listaNotificaciones.forEach(notificacion -> {
            notificacion.setEstado("leido");
            notificacionService.deleteEntity(notificacion);
        });
        attributes.addFlashAttribute("success", "Se han eliminado todas las notificaciones almacenadas");
        return "redirect:/leerNotificaciones";
    }
}
