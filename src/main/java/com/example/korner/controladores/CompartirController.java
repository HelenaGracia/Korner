package com.example.korner.controladores;

import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping
public class CompartirController {

    private final CompartirServiceImpl compartirService;
    private final AmigoServiceImpl amigoService;
    private final AnimeServiceImpl animeService;
    private final PeliculaServiceImpl peliculaService;
    private final LibroServiceImpl libroService;
    private final VideojuegoServiceImpl videojuegoService;
    private final SerieServiceImpl serieService;
    private final UsuarioSecurityService usuarioService;

    public CompartirController(CompartirServiceImpl compartirService, AmigoServiceImpl amigoService, AnimeServiceImpl animeService, PeliculaServiceImpl peliculaService, LibroServiceImpl libroService, VideojuegoServiceImpl videojuegoService, SerieServiceImpl serieService, UsuarioSecurityService usuarioService) {
        this.compartirService = compartirService;
        this.amigoService = amigoService;
        this.animeService = animeService;
        this.peliculaService = peliculaService;
        this.libroService = libroService;
        this.videojuegoService = videojuegoService;
        this.serieService = serieService;
        this.usuarioService = usuarioService;
    }


    @GetMapping("/compartir/{idPelicula}")
    public String compartirAmigoPelicula(@PathVariable(value = "idPelicula",required = false)Integer idPelicula,
                                    @RequestParam(value = "page") Optional<Integer> page,
                                    Model model, HttpSession session) {

     
        Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
        model.addAttribute("pelicula",pelicula.get());

        paginacion(page, model, session);

        return "compartirElementos";

    }

    @GetMapping("/compartir/{idAnime}")
    public String compartirAmigoAnime(@PathVariable(value = "idAnime",required = false)Integer idAnime,
                                 @RequestParam(value = "page") Optional<Integer> page,
                                 Model model, HttpSession session) {


        Optional<Anime> anime = animeService.getById(idAnime);
            model.addAttribute("anime",anime.get());

        paginacion(page, model, session);

        return "compartirElementos";

    }

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
        model.addAttribute("amigo", pagina.getContent());
    }

    @GetMapping("/compartir/searchAmigos")
    public String compartirAmigoBuscar(@RequestParam(value = "idPelicula",required = false)Integer idPelicula,
                                    @RequestParam(value = "idSerie",required = false)Integer idSerie,
                                    @RequestParam(value = "idAnime",required = false)Integer idAnime,
                                    @RequestParam(value = "idLibro",required = false)Integer idLibro,
                                    @RequestParam(value = "idVideojuegos",required = false)Integer idVideojuegos,
                                    @RequestParam(value = "page") Optional<Integer> page,
                                    @RequestParam(value = "nombreAmigo",required = false) String nombreAmigo,
                                    Model model, HttpSession session) {

        if (idPelicula != null) {
            Optional<Pelicula> pelicula = peliculaService.getById(idPelicula);
            model.addAttribute("pelicula",pelicula.get());
        }else if (idSerie != null) {
            Optional<Serie> serie = serieService.getById(idSerie);
            model.addAttribute("serie",serie.get());
        } else if (idAnime != null) {
            Optional<Anime> anime = animeService.getById(idAnime);
            model.addAttribute("anime",anime.get());
        } else if (idVideojuegos != null) {
            Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuegos);
            model.addAttribute("videojuego",videojuego.get());
        } else if (idLibro != null) {
            Optional<Libro> libro = libroService.getById(idLibro);
            model.addAttribute("libro",libro.get());
        }

        paginacion(page, model, session);
        return "compartirElementos";

    }

}//        ElementoCompartido peliculaCompartida = new ElementoCompartido();
//        peliculaCompartida.setPelicula(pelicula);
//        amigo.getElementoCompartidos().add(peliculaCompartida);
//        compartirService.saveEntity(peliculaCompartida);
//        amigoService.saveEntity(amigo);
//
//@PathVariable(value = "idSerie",required = false)Integer idSerie,
//@PathVariable(value = "idAnime",required = false)Integer idAnime,
//@PathVariable(value = "idLibro",required = false)Integer idLibro,
//@PathVariable(value = "idVideojuegos",required = false)Integer idVideojuegos,
//}else if (idSerie != null) {
//Optional<Serie> serie = serieService.getById(idSerie);
//            model.addAttribute("serie",serie.get());
//        } else if (idAnime != null) {
//Optional<Anime> anime = animeService.getById(idAnime);
//            model.addAttribute("anime",anime.get());
//        } else if (idVideojuegos != null) {
//Optional<Videojuego> videojuego = videojuegoService.getById(idVideojuegos);
//            model.addAttribute("videojuego",videojuego.get());
//        } else if (idLibro != null) {
//Optional<Libro> libro = libroService.getById(idLibro);
//            model.addAttribute("libro",libro.get());
//        }