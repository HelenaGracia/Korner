package com.example.korner.controladores;

import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/videojuegos")
public class VideojuegoController {

    private final VideojuegoServiceImpl videojuegoService;

    private final GeneroElementoServiceImpl generoElementoService;

    private final PlataformaVideojuegoServiceImpl plataformaVideojuegoService;
    private final FileSystemStorageService fileSystemStorageService;

    private final UsuarioSecurityService usuarioSecurityService;



    public VideojuegoController(VideojuegoServiceImpl videojuegoService,
                                GeneroElementoServiceImpl generoElementoService,
                                FileSystemStorageService fileSystemStorageService,
                                PlataformaVideojuegoServiceImpl plataformaVideojuegoService, UsuarioSecurityService usuarioSecurityService) {
        this.videojuegoService = videojuegoService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.plataformaVideojuegoService = plataformaVideojuegoService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    private final Logger logger = LoggerFactory.getLogger(VideojuegoController.class);

    //Mostrar Peliculas
    @GetMapping("")
    public String listAllVideojuegos(Model model, @RequestParam("page") Optional<Integer> page,
                                     HttpSession session, @RequestParam(value = "orden", required = false) String orden){


        paginacion(model, page, session, orden);



        //List<Pelicula> listadoPeliculas = peliculaService.getAll();
        Videojuego videojuego = new Videojuego();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<PlataformaVideojuego> plataformasList = plataformaVideojuegoService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("datosVideojuego", videojuego);

        return "videojuegos";
    }


    //Guardar Pelicula

    @PostMapping("/saveVideojuego")
    /*Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y
      se lo pasamos al parametro multipartFile
     */
    public String saveVideojuego(@RequestParam("imagen") MultipartFile multipartFile,
                               @Validated @ModelAttribute(name = "datosVideojuego") Videojuego videojuego,
                               BindingResult bindingResult, RedirectAttributes attributes,Model model,
                               @RequestParam("page") Optional<Integer> page, HttpSession session,
                               @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);



        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        Set<GeneroElementoCompartido> listadoGeneros = videojuego.getGenerosVideojuegos();
        logger.info("listado de generos:{}", listadoGeneros);
        List<PlataformaVideojuego> plataformasList = plataformaVideojuegoService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        if (bindingResult.hasErrors() || multipartFile.isEmpty()){
            if (multipartFile.isEmpty()){
                ObjectError error = new ObjectError("imagenError", "Debes seleccionar una imagen");
                bindingResult.addError(error);
                attributes.addFlashAttribute("failed", "Error al introducir la imagen, debe seleccionar una");
                model.addAttribute("videojuegoActual", -1);
                return "videojuegos";
            }
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            model.addAttribute("videojuegoActual", -1);
            return "videojuegos";

        }else {
            try {
                Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

                videojuego.setUsuarioVideojuego(user.get());
                logger.info("este es el objeto videojuego recibido{}", videojuego);
            /*guardamos en la BBDD  el objeto pelicula con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                videojuegoService.saveEntity(videojuego);

                logger.info("este es el objeto videojuego guardado{}", videojuego);
            /*Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id
             del objeto pelicula y la extensión del archivo(jpg, png)
             */
                String nombreArchivo = "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metodo y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                //Modificamos el nombre del atributo imagenRuta del objeto pelicula con la url que genera el controlador ImagenesController
                videojuego.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
                //Volvemos a guardar el objeto en la BBDD con los cambios
                videojuegoService.saveEntity(videojuego);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al guardar el videojuego creado", e);
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar el videojuego creado", e);
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/videojuegos";
        }

    }


    @PostMapping("/saveVideojuegoModificar")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String saveVideojuegoModificar(@RequestParam("imagen") MultipartFile multipartFile,
                                        @Validated @ModelAttribute(name = "datosVideojuego") Videojuego videojuego,
                                        BindingResult bindingResult,
                                        RedirectAttributes attributes, Model model,
                                        @RequestParam("page") Optional<Integer> page, HttpSession session,
                                        @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);




        final String FILE_PATH_ROOT = "D:/ficheros";
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<PlataformaVideojuego> plataformasList = plataformaVideojuegoService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        videojuego.setUsuarioVideojuego(user.get());

        if (bindingResult.hasErrors()){
            model.addAttribute("videojuegoActual", videojuego.getId());
            return "videojuegos";
        }else {
            try {
                if (multipartFile.isEmpty()){


                    Boolean archivo = Files.exists(Path.of(FILE_PATH_ROOT+"/" + ( "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId()  + ".jpg")));

                    if (archivo.equals(true)){
                        videojuego.setImagenRuta("/imagenes/leerImagen/" + "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId()  + ".jpg");
                    }else {
                        videojuego.setImagenRuta("/imagenes/leerImagen/" + "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId()  + ".png");
                    }

                } else{
                    //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por String Pelicula el id del objeto pelicula el titulo del objeto pelicula y la extensión del archivo(jpg, png)
                    String nombreArchivo = "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                    //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)

                    if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId() + ".jpg")))) {
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId() +".jpg"));
                    } else{
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Videojuego" + videojuego.getId() + "Usuario" + videojuego.getUsuarioVideojuego().getId() +".png"));

                    }
                    fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);

                    //Modificamos el nombre del atributo imagenRuta del objeto pelicula con la url que genera el controlador ImagenesController
                    videojuego.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);

                }

                //Volvemos a guardar el objeto en la BBDD con los cambios
                videojuegoService.saveEntity(videojuego);
                attributes.addFlashAttribute("success","Elemento añadido correctamente");
            } catch (DataIntegrityViolationException e){
                logger.error("Error al guardar el videojuego modificado");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar el videojuego modificado");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/videojuegos";
        }

    }


    //Eliminar Pelicula
    @PostMapping("/deleteVideojuego")
    public String deleteVideojuego(Videojuego videojuego, RedirectAttributes attributes){
        try {
            logger.info("este es el objeto videojuego eliminado{}", videojuego);
            videojuegoService.deleteEntity(videojuego);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            logger.error("Error al elminar videojuego", e);
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }

        return "redirect:/videojuegos";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "tituloVideojuegoBusqueda", required = false) String tituloVideojuegoBusqueda,
                         @RequestParam(value = "filtroPuntuacion", required = false) Integer filtroPuntuacion,
                         @RequestParam(value = "filtroGenero", required = false) Integer generoId,
                         @RequestParam(value = "filtroYear", required = false) Integer filtroYear,
                         @RequestParam(value = "filtroPlataforma", required = false) Integer plataformaId,
                         @RequestParam(value = "filtroOrden",required = false) String filtrOrden,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         HttpSession session, RedirectAttributes attributes) {
        logger.info("Titulo del vieojuego: {}", tituloVideojuegoBusqueda);
        logger.info("puntuacion recibida del filtro: {}", filtroPuntuacion);
        logger.info("genero recibido del filtro:{}", generoId);
        Videojuego videojuego = new Videojuego();
        model.addAttribute("datosVideojuego", videojuego);
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<PlataformaVideojuego> plataformasList = plataformaVideojuegoService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        videojuego.setUsuarioVideojuego(user.get());

        calcularAniosUsuario(model, user);

        try {
            // Determinar la página actual y configurar la paginación
            int currentPage = page.orElse(1);
            Pageable pageRequest = null;

            if (filtrOrden == null || filtrOrden.isBlank()){
                pageRequest = PageRequest.of(currentPage - 1, 4);
            }else {
                switch (filtrOrden){
                    case "ordenFiltroTituloAsc":
                        pageRequest = PageRequest.of(currentPage-1,4, Sort.by("titulo").ascending());
                        break;
                    case "ordenFiltroTituloDesc":
                        pageRequest = PageRequest.of(currentPage-1,4, Sort.by("titulo").descending());
                        break;
                    case "ordenFiltroIdAsc":
                        pageRequest = PageRequest.of(currentPage-1,4, Sort.by("id").ascending());
                        break;
                    case "ordenFiltroIdDesc":
                        pageRequest = PageRequest.of(currentPage-1,4, Sort.by("id").descending());
                }
                model.addAttribute("ordenFiltro", filtrOrden);
            }



            // Empiezan los filtros de búsqueda
            Page<Videojuego> pagina = null;
            Optional<PlataformaVideojuego> plataformaFiltro;
            Optional<GeneroElementoCompartido> generoFiltro;

            if(tituloVideojuegoBusqueda == null || tituloVideojuegoBusqueda.isBlank()){
                if (filtroPuntuacion!=null && generoId == null && filtroYear == null && plataformaId == null){
                    pagina = videojuegoService.getAllVideojuegosByPuntuacion(filtroPuntuacion, user.get(), pageRequest);
                    model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ningún videojuego con esa puntuación");
                        return "redirect:/videojuegos";
                    }

                } else if (filtroPuntuacion == null && generoId != null && filtroYear == null && plataformaId == null) {
                    generoFiltro = generoElementoService.getById(generoId);
                    if (generoFiltro.isPresent()){
                        pagina = videojuegoService.getAllVideojuegosByGenero(generoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("generoFiltro", generoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún videojuego con ese género");
                            return "redirect:/videojuegos";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "El género no existe");
                        return "redirect:/videojuegos";
                    }

                } else if (filtroPuntuacion == null && generoId == null && filtroYear != null && plataformaId == null) {
                    pagina = videojuegoService.getAllVideojuegosByYear(filtroYear, user.get(), pageRequest);
                    model.addAttribute("yearFiltro", filtroYear);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ningún videojuego con ese año");
                        return "redirect:/videojuegos";
                    }
                } else if (filtroPuntuacion == null && generoId == null && filtroYear == null && plataformaId != null) {
                    plataformaFiltro = plataformaVideojuegoService.getById(plataformaId);
                    if (plataformaFiltro.isPresent()){
                        pagina = videojuegoService.getAllVideojuegosByPlataforma(plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún videojuego con esa plataforma");
                            return "redirect:/videojuegos";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "La plataforma no existe");
                        return "redirect:/videojuegos";
                    }
                } else if (filtroPuntuacion != null && generoId != null && filtroYear != null && plataformaId != null) {
                    plataformaFiltro = plataformaVideojuegoService.getById(plataformaId);
                    generoFiltro = generoElementoService.getById(generoId);
                    if (plataformaFiltro.isPresent() && generoFiltro.isPresent()){
                        pagina = videojuegoService.getAllVideojuegosByAllFiltros(filtroPuntuacion, generoFiltro.get(),
                                filtroYear, plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                        model.addAttribute("generoFiltro", generoId);
                        model.addAttribute("yearFiltro", filtroYear);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún videojuego con esos filtros");
                            return "redirect:/videojuegos";
                        }
                    }

                }else {
                    attributes.addFlashAttribute("failed", "Sólo se puede filtrar por título, género, año, valoración " +
                            "plataforma de forma idividual o por género, año, valoración y plataforma juntos");
                    return "redirect:/videojuegos";
                }
            }else {
                pagina = videojuegoService.getAllVideojuegosByTitulo(tituloVideojuegoBusqueda, user.get(), pageRequest);
                if(pagina.getContent().isEmpty()){
                    attributes.addFlashAttribute("failed", "No hay videojuegos con ese título");
                    return "redirect:/videojuegos";
                }
                model.addAttribute("titulo", tituloVideojuegoBusqueda);
            }


            // Agregar resultados al modelo
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
            model.addAttribute("videojuegos", pagina.getContent());
        }catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");
        }
        return "videojuegos";
    }

    private void paginacion(Model model, Optional<Integer> page, HttpSession session, String orden){
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));

        calcularAniosUsuario(model,user);

        //Recibe la pagina en la que estoy si no recibe nada asigna la pagina 1
        int currentPage = page.orElse(1);
        //Guarda la pagina en la que estoy (Si es la pagina 1, la 2...) y la cantidad de elementos que quiero mostrar en ella
        PageRequest pageRequest = null;

        //Ordenacion
        if (orden == null || orden.isBlank()){
            pageRequest = PageRequest.of(currentPage - 1, 4);
        }else {
            switch (orden){
                case "ordenTituloAsc":
                    pageRequest = PageRequest.of(currentPage-1,4, Sort.by("titulo").ascending());
                    break;
                case "ordenTituloDesc":
                    pageRequest = PageRequest.of(currentPage-1,4, Sort.by("titulo").descending());
                    break;
                case "ordenIdAsc":
                    pageRequest = PageRequest.of(currentPage-1,4, Sort.by("id").ascending());
                    break;
                case "ordenIdDesc":
                    pageRequest = PageRequest.of(currentPage-1,4, Sort.by("id").descending());
            }
            model.addAttribute("orden", orden);
        }

        /*
         se crea un objeto page que es el encargado de rellenar en la pagina que le has indicado con la cantidad
         que le has dicho todos los objetos pelicula almacenados, es decir, crea la pagina que visualizas con el contenido
         */
        Page<Videojuego> pagina = videojuegoService.getAllVideojuegos(user.get(), pageRequest);

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

        model.addAttribute("videojuegos", pagina.getContent());






    }

    private void calcularAniosUsuario(Model model, Optional<Usuario> user) {
        //Obneter Listado con los años desde que el usuario nació hasta el año actual
        Integer actualYear = Year.now().getValue();
        Integer yearNacimiento = user.get().getAnioNacimiento();
        List<Integer> yearsDeVida = new ArrayList<>();
        for (Integer i = yearNacimiento; i<= actualYear; i++){
            yearsDeVida.add(i);
        }

        //Pasar el listado a la vista para que en las opciones del filtro por año de visualizacion aparezcan estos años
        model.addAttribute("listadoYears", yearsDeVida);
        //Pasar a la vista el año de nacimiento y el año actual para la validación del campo año de visualización
        model.addAttribute("anioActual", actualYear);
        model.addAttribute("anioNacimiento", yearNacimiento);
    }


}