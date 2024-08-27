package com.example.korner.controladores;

import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CompartidosController {
  
    private final CompartirServiceImpl compartirService;
    private final AmigoServiceImpl amigoService;
    private final PeliculaServiceImpl peliculaService;
    private final UsuarioSecurityService usuarioService;
    private final AnimeServiceImpl animeService;
    private final VideojuegoServiceImpl videojuegoService;
    private final LibroServiceImpl libroService;
    private final SerieServiceImpl serieService;
 
    public CompartidosController(CompartirServiceImpl compartirService, AmigoServiceImpl amigoService, PeliculaServiceImpl peliculaService, UsuarioSecurityService usuarioService, AnimeServiceImpl animeService, VideojuegoServiceImpl videojuegoService, LibroServiceImpl libroService, SerieServiceImpl serieService) {
        this.compartirService = compartirService;
        this.amigoService = amigoService;
        this.peliculaService = peliculaService;
        this.usuarioService = usuarioService;
        this.animeService = animeService;
        this.videojuegoService = videojuegoService;
        this.libroService = libroService;
        this.serieService = serieService;
    }


    /**
     * Este método se encarga de mostrar la lista de películas páginada que han sido comportidas al usuario por sus amigos.
     * También permite filtrar las películas por el nombre de usuario de un amigo específico.
     * @param nombreUsuario Permite filtrar las películas compartidas por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param page número de página para la paginación
     * @param session  permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/compartidos/peliculas")
    public String compartidosPeliculas(@RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                              @RequestParam(value = "page") Optional<Integer> page,
                              HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        List<ElementoCompartido> listElementosCompartidos = compartirService.getAllAmigosByAmigoOrigen(user.get());

        List<Integer> listPeliculaId = listElementosCompartidos.stream().map(ElementoCompartido::getPelicula).filter(pelicula -> pelicula != null).map(Pelicula::getId).toList();

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);
        Page<Pelicula> pagina=null ;

        if(nombreUsuario== null || nombreUsuario.isBlank()){
            pagina = peliculaService.getAllPeliculasCompartidosByListId(listPeliculaId, pageRequest);
            model.addAttribute("peliculas",pagina.getContent());
        }else{
            model.addAttribute("nombreUsuario", nombreUsuario);
            Optional<Usuario> usuarioBusqueda = usuarioService.getByNameUsuarioActivo(nombreUsuario);
            if(usuarioBusqueda.isPresent()){
                pagina= peliculaService.getAllPeliculasCompartidosByListIdAndUsuario(listPeliculaId, usuarioBusqueda.get(), pageRequest);
                Amigo amigoExiste = amigoService.getAmigo(usuarioBusqueda.get(),user.get());
                if(amigoExiste != null){
                    if(pagina.getContent().isEmpty()){
                        pagina = peliculaService.getAllPeliculasCompartidosByListId(listPeliculaId, pageRequest);
                        model.addAttribute("failed", "El usuario " + nombreUsuario + " no ha compartido ninguna película contigo");
                        model.addAttribute("peliculas", pagina.getContent());
                    } else {
                        model.addAttribute("peliculas",pagina.getContent());
                    }
                } else {
                    pagina = peliculaService.getAllPeliculasCompartidosByListId(listPeliculaId, pageRequest);
                    model.addAttribute("failed", "El usuario " + nombreUsuario + " no es amigo tuyo");
                    model.addAttribute("peliculas",pagina.getContent());
                }

            } else {
                model.addAttribute("failed", "El usuario " + nombreUsuario + " no existe");
                pagina = peliculaService.getAllPeliculasCompartidosByListId(listPeliculaId, pageRequest);
                model.addAttribute("peliculas",pagina.getContent());
            }
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
        return "peliculasCompartidas";
    }

    /**
     * Este método se encarga de mostrar la lista de animes páginada que han sido comportidos al usuario por sus amigos.
     * También permite filtrar los animes por el nombre de usuario de un amigo específico.
     * @param nombreUsuario Permite filtrar los animes compartidas por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param page número de página para la paginación
     * @param session  permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/compartidos/animes")
    public String compartidosAnimes(@RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                                       @RequestParam(value = "page") Optional<Integer> page,
                                       HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        List<ElementoCompartido> listElementosCompartidos = compartirService.getAllAmigosByAmigoOrigen(user.get());

        List<Integer> listAnimeId = listElementosCompartidos.stream().map(ElementoCompartido::getAnime).filter(anime -> anime != null).map(Anime::getId).toList();

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);
        Page<Anime> pagina;

        if(nombreUsuario== null || nombreUsuario.isBlank()){
            pagina = animeService.getAllAnimesCompartidosByListId(listAnimeId, pageRequest);
            model.addAttribute("animes",pagina.getContent());
        }else{
            model.addAttribute("nombreUsuario", nombreUsuario);
            Optional<Usuario> usuarioBusqueda = usuarioService.getByNameUsuarioActivo(nombreUsuario);
            if(usuarioBusqueda.isPresent()){
                pagina= animeService.getAllAnimesCompartidosByListIdAndUsuario(listAnimeId, usuarioBusqueda.get(), pageRequest);
                Amigo amigoExiste = amigoService.getAmigo(usuarioBusqueda.get(),user.get());
                if(amigoExiste != null){
                    if(pagina.getContent().isEmpty()){
                        pagina = animeService.getAllAnimesCompartidosByListId(listAnimeId, pageRequest);
                        model.addAttribute("failed", "El usuario " + nombreUsuario + " no ha compartido ningun anime contigo");
                        model.addAttribute("animes", pagina.getContent());
                    } else {
                        model.addAttribute("animes",pagina.getContent());
                    }
                } else {
                    pagina = animeService.getAllAnimesCompartidosByListId(listAnimeId, pageRequest);
                    model.addAttribute("failed", "El usuario " + nombreUsuario + " no es amigo tuyo");
                    model.addAttribute("animes",pagina.getContent());
                }

            } else {
                model.addAttribute("failed", "El usuario " + nombreUsuario + " no existe");
                pagina = animeService.getAllAnimesCompartidosByListId(listAnimeId, pageRequest);
                model.addAttribute("animes",pagina.getContent());
            }
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
        return "animesCompartidos";
    }

    /**
     * Este método se encarga de mostrar la lista de videojuegos páginada que han sido comportidos al usuario por sus amigos.
     * También permite filtrar los videojuegos por el nombre de usuario de un amigo específico.
     * @param nombreUsuario Permite filtrar los videojuegos compartidas por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param page número de página para la paginación
     * @param session  permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */
  @GetMapping("/compartidos/videojuegos")
    public String compartidosVideojuegos(@RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                                       @RequestParam(value = "page") Optional<Integer> page,
                                       HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

      model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
      model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        List<ElementoCompartido> listElementosCompartidos = compartirService.getAllAmigosByAmigoOrigen(user.get());
 
        List<Integer> listVideojuegoId = listElementosCompartidos.stream().map(ElementoCompartido::getVideojuego).filter(videojuego -> videojuego != null).map(Videojuego::getId).toList();
 
        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);
        Page<Videojuego> pagina=null ;
 
        if(nombreUsuario== null || nombreUsuario.isBlank()){
            pagina = videojuegoService.getAllVideojuegosCompartidosByListId(listVideojuegoId,pageRequest);
            model.addAttribute("videojuegos",pagina.getContent());
        }else{
            model.addAttribute("nombreUsuario", nombreUsuario);
            Optional<Usuario> usuarioBusqueda = usuarioService.getByNameUsuarioActivo(nombreUsuario);
            if(usuarioBusqueda.isPresent()){
                pagina = videojuegoService.getAllVideojuegosCompartidosByListIdAndUsuario(listVideojuegoId,usuarioBusqueda.get(), pageRequest);
                Amigo amigoExiste = amigoService.getAmigo(usuarioBusqueda.get(),user.get());
                if(amigoExiste != null){
                    if(pagina.getContent().isEmpty()){
                        pagina = videojuegoService.getAllVideojuegosCompartidosByListId(listVideojuegoId,pageRequest);
                        model.addAttribute("failed", "El usuario " + nombreUsuario + " no ha compartido ningún videojuego contigo");
                        model.addAttribute("videojuegos", pagina.getContent());
                    } else {
                        model.addAttribute("videojuegos",pagina.getContent());
                    }
                } else {
                    pagina = videojuegoService.getAllVideojuegosCompartidosByListId(listVideojuegoId,pageRequest);
                    model.addAttribute("failed", "El usuario " + nombreUsuario + " no es amigo tuyo");
                    model.addAttribute("videojuegos",pagina.getContent());
                }
 
            } else {
                model.addAttribute("failed", "El usuario " + nombreUsuario + " no existe");
                pagina = videojuegoService.getAllVideojuegosCompartidosByListId(listVideojuegoId,pageRequest);
                model.addAttribute("videojuegos",pagina.getContent());
            }
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
        return "videojuegosCompartidos";
    }

    /**
     * Este método se encarga de mostrar la lista de libros páginada que han sido comportidos al usuario por sus amigos.
     * También permite filtrar los libros por el nombre de usuario de un amigo específico.
     * @param nombreUsuario Permite filtrar los libros compartidas por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param page número de página para la paginación
     * @param session  permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/compartidos/libros")
    public String compartidosLibros(@RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                                         @RequestParam(value = "page") Optional<Integer> page,
                                         HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        List<ElementoCompartido> listElementosCompartidos = compartirService.getAllAmigosByAmigoOrigen(user.get());

        List<Integer> listlibroId = listElementosCompartidos.stream().map(ElementoCompartido::getLibro).filter(libro -> libro != null).map(Libro::getId).toList();

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);
        Page<Libro> pagina=null ;

        if(nombreUsuario== null || nombreUsuario.isBlank()){
            pagina = libroService.getAllLibroCompartidosByListId(listlibroId,pageRequest);
            model.addAttribute("libros",pagina.getContent());
        }else{
            model.addAttribute("nombreUsuario", nombreUsuario);
            Optional<Usuario> usuarioBusqueda = usuarioService.getByNameUsuarioActivo(nombreUsuario);
            if(usuarioBusqueda.isPresent()){
                pagina = libroService.getAllLibroCompartidosByListIdAndUsuario(listlibroId,usuarioBusqueda.get(), pageRequest);
                Amigo amigoExiste = amigoService.getAmigo(usuarioBusqueda.get(),user.get());
                if(amigoExiste != null){
                    if(pagina.getContent().isEmpty()){
                        pagina = libroService.getAllLibroCompartidosByListId(listlibroId,pageRequest);
                        model.addAttribute("failed", "El usuario " + nombreUsuario + " no ha compartido ningún libro contigo");
                        model.addAttribute("libros", pagina.getContent());
                    } else {
                        model.addAttribute("libros",pagina.getContent());
                    }
                } else {
                    pagina = libroService.getAllLibroCompartidosByListId(listlibroId,pageRequest);
                    model.addAttribute("failed", "El usuario " + nombreUsuario + " no es amigo tuyo");
                    model.addAttribute("libros",pagina.getContent());
                }

            } else {
                model.addAttribute("failed", "El usuario " + nombreUsuario + " no existe");
                pagina = libroService.getAllLibroCompartidosByListId(listlibroId,pageRequest);
                model.addAttribute("libros",pagina.getContent());
            }
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
        return "librosCompartidos";
    }

    /**
     * Este método se encarga de mostrar la lista de series páginada que han sido comportidas al usuario por sus amigos.
     * También permite filtrar las series por el nombre de usuario de un amigo específico.
     * @param nombreUsuario Permite filtrar las series compartidas por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param page número de página para la paginación
     * @param session  permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/compartidos/series")
    public String compartidosSeries(@RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                                       @RequestParam(value = "page") Optional<Integer> page,
                                       HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        List<ElementoCompartido> listElementosCompartidos = compartirService.getAllAmigosByAmigoOrigen(user.get());
        List<Integer> listSerieId = listElementosCompartidos.stream().map(ElementoCompartido::getSerie).filter(serie -> serie != null).map(Serie::getId).toList();

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);
        Page<Serie> pagina=null ;

        if(nombreUsuario== null || nombreUsuario.isBlank()){
            pagina = serieService.getAllSeriesCompartidosByListId(listSerieId, pageRequest);
            model.addAttribute("series",pagina.getContent());
        }else{
            model.addAttribute("nombreUsuario", nombreUsuario);
            Optional<Usuario> usuarioBusqueda = usuarioService.getByNameUsuarioActivo(nombreUsuario);
            if(usuarioBusqueda.isPresent()){
                pagina= serieService.getAllSeriesCompartidosByListIdAndUsuario(listSerieId, usuarioBusqueda.get(), pageRequest);
                Amigo amigoExiste = amigoService.getAmigo(usuarioBusqueda.get(),user.get());
                if(amigoExiste != null){
                    if(pagina.getContent().isEmpty()){
                        pagina = serieService.getAllSeriesCompartidosByListId(listSerieId, pageRequest);
                        model.addAttribute("failed", "El usuario " + nombreUsuario + " no ha compartido ninguna serie contigo");
                        model.addAttribute("series", pagina.getContent());
                    } else {
                        model.addAttribute("series",pagina.getContent());
                    }
                } else {
                    pagina = serieService.getAllSeriesCompartidosByListId(listSerieId, pageRequest);
                    model.addAttribute("failed", "El usuario " + nombreUsuario + " no es amigo tuyo");
                    model.addAttribute("series",pagina.getContent());
                }

            } else {
                model.addAttribute("failed", "El usuario " + nombreUsuario + " no existe");
                pagina = serieService.getAllSeriesCompartidosByListId(listSerieId, pageRequest);
                model.addAttribute("series",pagina.getContent());
            }
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
        return "seriesCompartidas";
    }
}