package com.example.korner.controladores;

import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
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

public class CompartirController {

    private final CompartirServiceImpl compartirService;
    private final AmigoServiceImpl amigoService;
    private final AnimeServiceImpl animeService;
    private final PeliculaServiceImpl peliculaService;
    private final LibroServiceImpl libroService;
    private final VideojuegoServiceImpl videojuegoService;
    private final SerieServiceImpl serieService;
    private final UsuarioSecurityService usuarioService;
    private final NotificacionService notificacionService;

    public CompartirController(CompartirServiceImpl compartirService, AmigoServiceImpl amigoService, AnimeServiceImpl animeService, PeliculaServiceImpl peliculaService, LibroServiceImpl libroService, VideojuegoServiceImpl videojuegoService, SerieServiceImpl serieService, UsuarioSecurityService usuarioService, NotificacionService notificacionService) {
        this.compartirService = compartirService;
        this.amigoService = amigoService;
        this.animeService = animeService;
        this.peliculaService = peliculaService;
        this.libroService = libroService;
        this.videojuegoService = videojuegoService;
        this.serieService = serieService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
    }

    private final Logger logger = LoggerFactory.getLogger(CompartirController.class);


    /**
     * Este método se encarga de preparar la acción de compartir una película con amigos. Primero, intenta obtener la
     * película basada en el ID proporcionado, y si tiene éxito, almacena ese ID en el modelo. Si ocurre algún error,
     * registra el error y añade un mensaje de fallo al modelo.
     * @param idPelicula parámetro que representa el ID de la película que se va a compartir
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirige al usuario a una página de búsqueda de amigos, pasando la información necesaria a través de
     * la URL para que el usuario pueda completar el proceso de compartir la película
     */
    @GetMapping("/compartir/pelicula/{idPelicula}")
    public String compartirAmigoPelicula(@PathVariable(value = "idPelicula",required = false)Integer idPelicula,
                                    Model model) {
        try {
            Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
            model.addAttribute("peliculaEnviadaId",pelicula.get().getId());

        }catch (Exception e){
            logger.error("Error en el proceso de compartir una película", e);
            model.addAttribute("failed", "Error en el proceso de compartir una película");
        }
        return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=" + idPelicula +"&idSerie=&idAnime=&idLibro=&idVideojuegos=";

    }

    /**
     * Este método se encarga de preparar la acción de compartir una serie con amigos. Primero, intenta obtener la
     * serie basada en el ID proporcionado, y si tiene éxito, almacena ese ID en el modelo. Si ocurre algún error,
     * registra el error y añade un mensaje de fallo al modelo.
     * @param idSerie parámetro que representa el ID de la serie que se va a compartir
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirige al usuario a una página de búsqueda de amigos, pasando la información necesaria a través de
     * la URL para que el usuario pueda completar el proceso de compartir la serie.
     */
    @GetMapping("/compartir/serie/{idSerie}")
    public String compartirAmigoSerie(@PathVariable(value = "idSerie",required = false)Integer idSerie,
                                      Model model) {

        try {
            Optional<Serie> serie = serieService.getById(idSerie);
            model.addAttribute("serieEnviadaId",serie.get().getId());

        }catch (Exception e){
            logger.error("Error en el proceso de compartir una serie", e);
            model.addAttribute("failed", "Error en el proceso de compartir una serie");
        }
        return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=" + idSerie + "&idAnime=&idLibro=&idVideojuegos=";

    }

    /**
     * Este método se encarga de preparar la acción de compartir un anime con amigos. Primero, intenta obtener el
     * anime basada en el ID proporcionado, y si tiene éxito, almacena ese ID en el modelo. Si ocurre algún error,
     * registra el error y añade un mensaje de fallo al modelo.
     * @param idAnime parámetro que representa el ID del anime que se va a compartir
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirige al usuario a una página de búsqueda de amigos, pasando la información necesaria a través de
     * la URL para que el usuario pueda completar el proceso de compartir el anime
     */
    @GetMapping("/compartir/anime/{idAnime}")
    public String compartirAmigoAnime(@PathVariable(value = "idAnime",required = false)Integer idAnime,
                                 Model model) {

        try {
            Optional<Anime> anime = animeService.getById(idAnime);
            model.addAttribute("animeEnviadoId",anime.get().getId());

        }catch (Exception e){
            logger.error("Error en el proceso de compartir un anime", e);
            model.addAttribute("failed", "Error en el proceso de compartir un anime");
        }

        return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime="+ idAnime+"&idLibro=&idVideojuegos=";

    }

    /**
     * Este método se encarga de preparar la acción de compartir un libro con amigos. Primero, intenta obtener el
     * anime basada en el ID proporcionado, y si tiene éxito, almacena ese ID en el modelo. Si ocurre algún error,
     * registra el error y añade un mensaje de fallo al modelo.
     * @param idLibro parámetro que representa el ID del libro que se va a compartir
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirige al usuario a una página de búsqueda de amigos, pasando la información necesaria a través de
     * la URL para que el usuario pueda completar el proceso de compartir el libro
     */
    @GetMapping("/compartir/libro/{idLibro}")
    public String compartirAmigoLibro(@PathVariable(value = "idLibro",required = false)Integer idLibro,
                                      Model model) {

        try {
            Optional<Libro> libro = libroService.getById(idLibro);
            model.addAttribute("libroEnviadoId",libro.get().getId());
            model.addAttribute("objetoCompartido", "Vas a compartir el libro: " + libro.get().getTitulo());

        }catch (Exception e){
            logger.error("Error en el proceso de compartir un libro", e);
            model.addAttribute("failed", "Error en el proceso de compartir un libro");
        }


        return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro="+ idLibro +"&idVideojuegos=";

    }

    /**
     * Este método se encarga de preparar la acción de compartir un videojuego con amigos. Primero, intenta obtener el
     * videojuego basada en el ID proporcionado, y si tiene éxito, almacena ese ID en el modelo. Si ocurre algún error,
     * registra el error y añade un mensaje de fallo al modelo.
     * @param idVideojuego parámetro que representa el ID del videojuego que se va a compartir
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirige al usuario a una página de búsqueda de amigos, pasando la información necesaria a través de
     * la URL para que el usuario pueda completar el proceso de compartir el videojuego
     */
    @GetMapping("/compartir/videojuego/{idVideojuegos}")
    public String compartirAmigoVideojuego(@PathVariable(value = "idVideojuegos",required = false)Integer idVideojuego,
                                      Model model) {

        try {
            Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuego);
            model.addAttribute("videojuegoEnviadoId",videojuego.get().getId());

        }catch (Exception e){
            logger.error("Error en el proceso de compartir un videojuego", e);
            model.addAttribute("failed", "Error en el proceso de compartir un videojuego");
        }


        return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro=&idVideojuegos=" + idVideojuego;

    }

    /**
     * Este método  facilita el proceso de buscar amigos con los que compartir contenido
     * específico (películas, series, animes, libros, videojuegos). Dependiendo de si el usuario proporciona un
     * nombre de amigo, el método busca amigos que coincidan con ese nombre y realiza la paginación para mostrar
     * los resultados. Si no se proporciona un nombre de amigo, el método simplemente prepara los datos del contenido
     * a compartir y muestra la página de compartir con toda la lista de amigos del usuario, sin realizar una búsqueda específica de amigos.
     * @param idPelicula identificador de la película que se va a compartir
     * @param idSerie identificador de la serie que se va a compartir
     * @param idAnime identificador del anime que se va a compartir
     * @param idLibro identificador del libro que se va a compartir
     * @param idVideojuegos identificador del videojuego que se va a compartir
     * @param page número de página para la paginación
     * @param nombreAmigo Permite filtrar los amigos del usuario por un amigo específico cuyo nombre de usuario es proporcionado desde el formulario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * @return String del nombre de la vista que debe ser renderizada, que es la página donde se muestran los amigos
     * y la opción de compartir el contenido seleccionado
     */
    @GetMapping("/compartir/searchAmigos")
    public String compartirAmigoBuscar(@RequestParam(value = "idPelicula",required = false)Integer idPelicula,
                                       @RequestParam(value = "idSerie",required = false)Integer idSerie,
                                       @RequestParam(value = "idAnime",required = false)Integer idAnime,
                                       @RequestParam(value = "idLibro",required = false)Integer idLibro,
                                       @RequestParam(value = "idVideojuegos",required = false)Integer idVideojuegos,
                                       @RequestParam(value = "page") Optional<Integer> page,
                                       @RequestParam(value = "nombreAmigo",required = false) String nombreAmigo,
                                       Model model, HttpSession session) {

        try {
            if (nombreAmigo == null || nombreAmigo.isBlank()){

                if (idPelicula != null) {
                    Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
                    model.addAttribute("peliculaEnviadaId",pelicula.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir la pelicula: " + pelicula.get().getTitulo());
                }else if (idSerie != null) {
                    Optional<Serie> serie = serieService.getById(idSerie);
                    model.addAttribute("serieEnviadaId",serie.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir la serie: " + serie.get().getTitulo());
                } else if (idAnime != null) {
                    Optional<Anime> anime = animeService.getById(idAnime);
                    model.addAttribute("animeEnviadoId",anime.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el anime: " + anime.get().getTitulo());
                } else if (idVideojuegos != null) {
                    Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuegos);
                    model.addAttribute("videojuegoEnviadoId",videojuego.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el videojuego: " + videojuego.get().getTitulo());
                } else if (idLibro != null) {
                    Optional<Libro> libro = libroService.getById(idLibro);
                    model.addAttribute("libroEnviadoId",libro.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el libro: " + libro.get().getTitulo());
                }

                paginacion(page, model, session);
                return "compartirElementos";
            }
            else {
                if (idPelicula != null) {
                    Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
                    model.addAttribute("peliculaEnviadaId",pelicula.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir la pelicula: " + pelicula.get().getTitulo());
                }else if (idSerie != null) {
                    Optional<Serie> serie = serieService.getById(idSerie);
                    model.addAttribute("serieEnviadaId",serie.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir la serie: " + serie.get().getTitulo());
                } else if (idAnime != null) {
                    Optional<Anime> anime = animeService.getById(idAnime);
                    model.addAttribute("animeEnviadoId",anime.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el anime: " + anime.get().getTitulo());
                } else if (idVideojuegos != null) {
                    Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuegos);
                    model.addAttribute("videojuegoEnviadoId",videojuego.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el videojuego: " + videojuego.get().getTitulo());
                } else if (idLibro != null) {
                    Optional<Libro> libro = libroService.getById(idLibro);
                    model.addAttribute("libroEnviadoId",libro.get().getId());
                    model.addAttribute("objetoCompartido", "Vas a compartir el libro: " + libro.get().getTitulo());
                }

                Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
                //lista Amigos del usuario no bloqueados y no pendientes
                List<Amigo> listAmigos = amigoService.getAllAmigosListNoBloqueadosNoPendientes(user.get());
                //lista de id usuariosDestino de esos amigos
                List<Integer> listIdAmigos= new java.util.ArrayList<>(listAmigos.stream().map(Amigo::getUsuarioDestino).map(Usuario::getId).toList());
                //Lista de usuarios filtrados por nombre que se encuentran dentro de la lista de id
                List<Usuario> listaUsuariosDestino = usuarioService.getAllUsuariosEnListId(nombreAmigo,listIdAmigos);
                int currentPage = page.orElse(1);
                Pageable pageRequest = PageRequest.of(currentPage - 1, 2);
                Page<Amigo> pagina = amigoService.getAllAmigosEnListaUsuarioDestino(user.get(),listaUsuariosDestino,pageRequest);
                model.addAttribute("busquedaAmigo",nombreAmigo);
                if(pagina.getContent().isEmpty() ){
                    model.addAttribute("failed","No se ha encontrado ningun usuario con ese nombre");
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
                model.addAttribute("amigos", pagina.getContent());
                model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
                model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
            }
        }catch (Exception e){
            logger.error("Error en el proceso de búsqueda y compartir", e);
            model.addAttribute("failed", "Error en el proceso de búsqueda y compartir");
            paginacion(page, model, session);
        }

        return "compartirElementos";

    }
    /**
     * Este método se encarga de gestionar la paginación de la lista de amigos del usuario de la sesión
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param page número de página para la paginación
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     *
     */

    private void paginacion(Optional<Integer> page, Model model, HttpSession session) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));

        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
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
        model.addAttribute("amigos", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
    }

    /**
     * Este método gestiona el proceso de compartir un contenido específico (película, serie, videojueo, anime, libro.)
     * con un amigo en la aplicación. Verifica si el contenido ya ha sido compartido previamente y si el amigo no
     * ha bloqueado al usuario. Si es correcto, guarda el contenido compartido y genera una notificación para el amigo.
     * Si no, muestra un mensaje de error y redirige al usuario de vuelta a la búsqueda de amigos.
     * @param idAmigo identificador del amigo con el que se compartirá el contenido
     * @param idPelicula identificador de la película que se va a compartir
     * @param idSerie identificador de la serie que se va a compartir
     * @param idAnime identificador del anime que se va a compartir
     * @param idLibro identificador del libro que se va a compartir
     * @param idVideojuegos identificador del videojuego que se va a compartir
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso mensajes
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos como el ID del usuario,
     * la imagen de perfil, y el nombre de usuario
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @return redirección a la página de búsqueda de amigos con el mensaje correspondiente (éxito o error).
     */

    @GetMapping("/compartir/añadir/{idAmigo}")
    public String add(@PathVariable(value = "idAmigo",required = false) Integer idAmigo,
                      @RequestParam(value = "idPelicula",required = false)Integer idPelicula,
                      @RequestParam(value = "idSerie",required = false)Integer idSerie,
                      @RequestParam(value = "idAnime",required = false)Integer idAnime,
                      @RequestParam(value = "idLibro",required = false)Integer idLibro,
                      @RequestParam(value = "idVideojuegos",required = false)Integer idVideojuegos,
                      RedirectAttributes attributes, HttpSession session, Model model){
        
        Optional<Amigo> amigoOrigen = amigoService.getById(idAmigo);
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

        Amigo amigoDestino = amigoService.getAmigo(amigoOrigen.get().getUsuarioOrigen(), amigoOrigen.get().getUsuarioDestino());
        if(amigoDestino.getBloqueado()){
            attributes.addFlashAttribute("failed", "Este usuario te tiene bloqueado");
            if(idPelicula !=null){
                return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=" + idPelicula +"&idSerie=&idAnime=&idLibro=&idVideojuegos=";
            } else if (idSerie != null) {
                return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=" + idSerie + "&idAnime=&idLibro=&idVideojuegos=";
            } else if (idAnime!= null) {
                return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime="+ idAnime+"&idLibro=&idVideojuegos=";
            } else if (idLibro !=null) {
                return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro="+ idLibro +"&idVideojuegos=";
            } else if (idVideojuegos !=null) {
                return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro=&idVideojuegos=" + idVideojuegos;
            }
        }
        else {
            ElementoCompartido elementoCompartido = new ElementoCompartido();
            if (idPelicula != null){
                Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
                List<ElementoCompartido> listaExiste = amigoDestino.getElementoCompartidos().stream().filter(elemento -> elemento.getPelicula() != null).filter(elemento2 -> elemento2.getPelicula().equals(pelicula.get())).toList();
                if (listaExiste.isEmpty()){
                    elementoCompartido.setPelicula(pelicula.get());
                    elementoCompartido.setAmigos(amigoDestino);
                    amigoDestino.getElementoCompartidos().add(elementoCompartido);
                    compartirService.saveEntity(elementoCompartido);
                    amigoService.saveEntity(amigoDestino);
                    /* Creamos el objeto Notificacion que albergará la información de que se ha compartido una película
                     esta notificación va destinada al usuarioDestino y le llegará a este */
                    Notificacion notificacion = new Notificacion();
                    String nombreUsuarioOrigen = session.getAttribute("userName").toString();
                    notificacion.setUserFrom(nombreUsuarioOrigen);
                    notificacion.setUserFromId(usuarioService.getByName(nombreUsuarioOrigen).get().getId());
                    notificacion.setUserTo(amigoDestino.getUsuarioOrigen().getNombre());
                    notificacion.setEstado("pendiente");
                    notificacion.setTipoElemento("pelicula");
                    notificacion.setIdTipoElemento(pelicula.get().getId());
                    notificacion.setMensaje("El usuario " +nombreUsuarioOrigen  + " ha compartido la película " + pelicula.get().getTitulo() + " contigo");
                    notificacion.setRutaImagenUserFrom(amigoDestino.getUsuarioDestino().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    attributes.addFlashAttribute("success", "Has compartido la película " + pelicula.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=" + idPelicula +"&idSerie=&idAnime=&idLibro=&idVideojuegos=";
                }else {
                    attributes.addFlashAttribute("failed", "Ya has compartido la película: " + pelicula.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=" + idPelicula +"&idSerie=&idAnime=&idLibro=&idVideojuegos=";
                }

            } else if (idSerie != null) {
                Optional<Serie> serie = serieService.getById(idSerie);
                List<ElementoCompartido> listaExiste = amigoDestino.getElementoCompartidos().stream().filter(elemento -> elemento.getSerie() != null).filter(elemento2 -> elemento2.getSerie().equals(serie.get())).toList();
                if (listaExiste.isEmpty()){
                    elementoCompartido.setSerie(serie.get());
                    elementoCompartido.setAmigos(amigoDestino);
                    amigoDestino.getElementoCompartidos().add(elementoCompartido);
                    compartirService.saveEntity(elementoCompartido);
                    amigoService.saveEntity(amigoDestino);
                    /* Creamos el objeto Notificacion que albergará la información de que se ha compartido una película
                     esta notificación va destinada al usuarioDestino y le llegará a este */
                    Notificacion notificacion = new Notificacion();
                    String nombreUsuarioOrigen = session.getAttribute("userName").toString();
                    notificacion.setUserFrom(nombreUsuarioOrigen);
                    notificacion.setUserFromId(usuarioService.getByName(nombreUsuarioOrigen).get().getId());
                    notificacion.setUserTo(amigoDestino.getUsuarioOrigen().getNombre());
                    notificacion.setEstado("pendiente");
                    notificacion.setTipoElemento("serie");
                    notificacion.setIdTipoElemento(serie.get().getId());
                    notificacion.setMensaje("El usuario " +nombreUsuarioOrigen  + " ha compartido la serie " + serie.get().getTitulo() + " contigo");
                    notificacion.setRutaImagenUserFrom(amigoDestino.getUsuarioDestino().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    attributes.addFlashAttribute("success", "Has compartido la serie " + serie.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=" + idSerie + "&idAnime=&idLibro=&idVideojuegos=";
                }else {
                    attributes.addFlashAttribute("failed", "Ya has compartido la serie: " + serie.get().getTitulo() + " con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=" + idSerie + "&idAnime=&idLibro=&idVideojuegos=";
                }
            } else if (idAnime != null) {
                Optional<Anime> anime = animeService.getById(idAnime);
                List<ElementoCompartido> listaExiste = amigoDestino.getElementoCompartidos().stream().filter(elemento -> elemento.getAnime() != null).filter(elemento2->elemento2.getAnime().equals(anime.get())).toList();
                if (listaExiste.isEmpty()){
                    elementoCompartido.setAnime(anime.get());
                    elementoCompartido.setAmigos(amigoDestino);
                    amigoDestino.getElementoCompartidos().add(elementoCompartido);
                    compartirService.saveEntity(elementoCompartido);
                    amigoService.saveEntity(amigoDestino);
                    Notificacion notificacion = new Notificacion();
                    String nombreUsuarioOrigen = session.getAttribute("userName").toString();
                    notificacion.setUserFrom(nombreUsuarioOrigen);
                    notificacion.setUserFromId(usuarioService.getByName(nombreUsuarioOrigen).get().getId());
                    notificacion.setUserTo(amigoDestino.getUsuarioOrigen().getNombre());
                    notificacion.setEstado("pendiente");
                    notificacion.setTipoElemento("anime");
                    notificacion.setIdTipoElemento(anime.get().getId());
                    notificacion.setMensaje("El usuario " +nombreUsuarioOrigen  + " ha compartido el anime " + anime.get().getTitulo() + " contigo");
                    notificacion.setRutaImagenUserFrom(amigoDestino.getUsuarioDestino().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    attributes.addFlashAttribute("success", "Has compartido el anime " + anime.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime="+ idAnime+"&idLibro=&idVideojuegos=";
                }else {
                    attributes.addFlashAttribute("failed", "Ya has compartido el anime: " + anime.get().getTitulo() + " con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime="+ idAnime+"&idLibro=&idVideojuegos=";
                }
            } else if (idLibro != null) {
                Optional<Libro> libro = libroService.getById(idLibro);
                List<ElementoCompartido> listaExiste = amigoDestino.getElementoCompartidos().stream().filter(elemento -> elemento.getLibro() != null).filter(elemento2 -> elemento2.getLibro().equals(libro.get())).toList();
                if (listaExiste.isEmpty()){
                    elementoCompartido.setLibro(libro.get());
                    elementoCompartido.setAmigos(amigoDestino);
                    amigoDestino.getElementoCompartidos().add(elementoCompartido);
                    compartirService.saveEntity(elementoCompartido);
                    amigoService.saveEntity(amigoDestino);
                    Notificacion notificacion = new Notificacion();
                    String nombreUsuarioOrigen = session.getAttribute("userName").toString();
                    notificacion.setUserFrom(nombreUsuarioOrigen);
                    notificacion.setUserFromId(usuarioService.getByName(nombreUsuarioOrigen).get().getId());
                    notificacion.setUserTo(amigoDestino.getUsuarioOrigen().getNombre());
                    notificacion.setEstado("pendiente");
                    notificacion.setTipoElemento("libro");
                    notificacion.setIdTipoElemento(libro.get().getId());
                    notificacion.setMensaje("El usuario " +nombreUsuarioOrigen  + " ha compartido el libro " + libro.get().getTitulo() + " contigo");
                    notificacion.setRutaImagenUserFrom(amigoDestino.getUsuarioDestino().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    attributes.addFlashAttribute("success", "Has compartido el libro " + libro.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro="+ idLibro +"&idVideojuegos=";
                }else {
                    attributes.addFlashAttribute("failed", "Ya has compartido el libro: " + libro.get().getTitulo() + " con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro="+ idLibro +"&idVideojuegos=";
                }
            } else if (idVideojuegos!=null) {
                Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuegos);
                List<ElementoCompartido> listaExiste = amigoDestino.getElementoCompartidos().stream().filter(elemento -> elemento.getVideojuego() != null).filter(elemento2 -> elemento2.getVideojuego().equals(videojuego.get())).toList();
                if (listaExiste.isEmpty()){
                    elementoCompartido.setVideojuego(videojuego.get());
                    elementoCompartido.setAmigos(amigoDestino);
                    amigoDestino.getElementoCompartidos().add(elementoCompartido);
                    compartirService.saveEntity(elementoCompartido);
                    amigoService.saveEntity(amigoDestino);
                    Notificacion notificacion = new Notificacion();
                    String nombreUsuarioOrigen = session.getAttribute("userName").toString();
                    notificacion.setUserFrom(nombreUsuarioOrigen);
                    notificacion.setUserFromId(usuarioService.getByName(nombreUsuarioOrigen).get().getId());
                    notificacion.setUserTo(amigoDestino.getUsuarioOrigen().getNombre());
                    notificacion.setEstado("pendiente");
                    notificacion.setTipoElemento("videojuego");
                    notificacion.setIdTipoElemento(videojuego.get().getId());
                    notificacion.setMensaje("El usuario " +nombreUsuarioOrigen  + " ha compartido el videojuego " + videojuego.get().getTitulo() + " contigo");
                    notificacion.setRutaImagenUserFrom(amigoDestino.getUsuarioDestino().getRutaImagen());
                    notificacionService.saveEntity(notificacion);
                    attributes.addFlashAttribute("success", "Has compartido el videojuego " + videojuego.get().getTitulo() +" con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro=&idVideojuegos=" + idVideojuegos;
                }else {
                    attributes.addFlashAttribute("failed", "Ya has compartido el videojuego: " + videojuego.get().getTitulo() + " con el usuario " + amigoOrigen.get().getUsuarioDestino().getNombre());
                    return "redirect:/compartir/searchAmigos?nombreAmigo=&idPelicula=&idSerie=&idAnime=&idLibro=&idVideojuegos=" + idVideojuegos;
                }
            }
        }

        return "redirect:/amigos";
    }

}