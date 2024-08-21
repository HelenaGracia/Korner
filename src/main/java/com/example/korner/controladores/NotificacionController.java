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

    public NotificacionController(NotificacionService notificacionService, PeliculaServiceImpl peliculaService, SerieServiceImpl serieService, LibroServiceImpl libroService, AnimeServiceImpl animeService, VideojuegoServiceImpl videojuegoService, UsuarioSecurityService usuarioSecurityService) {
        this.notificacionService = notificacionService;
        this.peliculaService = peliculaService;
        this.serieService = serieService;
        this.libroService = libroService;
        this.animeService = animeService;
        this.videojuegoService = videojuegoService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    @GetMapping("/numeroNotificaciones")
    public ResponseEntity<String> contarNotificacionesPendientes(HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"pendiente");
        listaNotificaciones.forEach(notificacion -> {
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
                            notificacionService.saveEntity(notificacion);
                        }
                    }else if (usuarioSecurityService.getByName(notificacion.getUserFrom()).isEmpty()){
                        notificacionService.deleteEntity(notificacion);
                    }
                    break;
                case "solicitud":
                    Optional<Usuario> usuario = usuarioSecurityService.getById(notificacion.getIdTipoElemento());
                    if (usuario.isEmpty()){
                        notificacionService.deleteEntity(notificacion);
                    }else {
                        if (!usuario.get().getActiva()){
                            notificacion.setEstadoUsuario("inactivo");
                            notificacionService.saveEntity(notificacion);
                        }
                        else {
                            notificacion.setEstadoUsuario("activo");
                            notificacionService.saveEntity(notificacion);
                        }
                    }break;
            }
        });
        List<Notificacion> listaNotificacionesActualizada = notificacionService.getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(nombreUsuario,"pendiente", "activo");
        return ResponseEntity.ok(String.valueOf(listaNotificacionesActualizada.size())) ;
    }


    @GetMapping("/leerNotificaciones")
    public String leerNotificaciones(Model model, @RequestParam("page") Optional<Integer> page, HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(nombreUsuario,"pendiente", "activo");
        listaNotificaciones.forEach(notificacion -> {
            notificacion.setEstado("leido");
            notificacionService.saveEntity(notificacion);
        });

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);;
        Page<Notificacion> pagina = notificacionService.getAllNotificacionesByUserAndEstado(nombreUsuario, "leido", pageRequest);
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
