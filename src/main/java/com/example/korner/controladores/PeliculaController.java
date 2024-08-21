package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.Usuario;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    private final PeliculaServiceImpl peliculaService;

    private final GeneroElementoServiceImpl generoElementoService;

    private final PlataformaServiceImpl plataformaService;
    private final FileSystemStorageService fileSystemStorageService;

    private final UsuarioSecurityService usuarioSecurityService;



    public PeliculaController(PeliculaServiceImpl peliculaService,
                              GeneroElementoServiceImpl generoElementoService,
                              FileSystemStorageService fileSystemStorageService,
                              PlataformaServiceImpl plataformaService, UsuarioSecurityService usuarioSecurityService) {
        this.peliculaService = peliculaService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.plataformaService = plataformaService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    private final Logger logger = LoggerFactory.getLogger(PeliculaController.class);

    //Mostrar Peliculas
    @GetMapping("")
    public String listAllPeliculas(Model model, @RequestParam("page") Optional<Integer> page,
                                   HttpSession session, @RequestParam(value = "orden", required = false) String orden){


        paginacion(model, page, session, orden);


        Pelicula pelicula = new Pelicula();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("datosPelicula", pelicula);

        return "peliculas";
    }


    //Guardar Pelicula

    @PostMapping("/savePelicula")
    /*Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y
      se lo pasamos al parametro multipartFile
     */
    public String savePelicula(@RequestParam("imagen") MultipartFile multipartFile,
                               @Validated @ModelAttribute(name = "datosPelicula") Pelicula pelicula,
                               BindingResult bindingResult, RedirectAttributes attributes,Model model,
                               @RequestParam("page") Optional<Integer> page, HttpSession session,
                               @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);



        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));


        if (bindingResult.hasErrors() || multipartFile.isEmpty()){
            if (multipartFile.isEmpty()){
                ObjectError error = new ObjectError("imagenError", "Debes seleccionar una imagen");
                bindingResult.addError(error);
                attributes.addFlashAttribute("failed", "Error al introducir la imagen, debe seleccionar una");
                model.addAttribute("peliculaActual", -1);
                return "peliculas";
            }
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            model.addAttribute("peliculaActual", -1);
            return "peliculas";

        } else if (peliculaService.getPeliculaByTituloAndUsuario(pelicula.getTitulo(),user.get()).isPresent()) {
            model.addAttribute("tituloRepetido", "Ya tienes una película con ese título");
            return "peliculas";
        } else {
            try {
                pelicula.setUsuarioPelicula(user.get());

            /*guardamos en la BBDD  el objeto pelicula con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                peliculaService.saveEntity(pelicula);

            /*Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id
             del objeto pelicula y la extensión del archivo(jpg, png)
             */
                String nombreArchivo = "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metodo y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                //Modificamos el nombre del atributo imagenRuta del objeto pelicula con la url que genera el controlador ImagenesController
                pelicula.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
                //Volvemos a guardar el objeto en la BBDD con los cambios
                peliculaService.saveEntity(pelicula);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al guardar la pelicula creada por nombres duplicados");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar la pelicula creada");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/peliculas";
        }

    }


    @PostMapping("/savePeliculaModificar")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String savePeliculaModificar(@RequestParam("imagen") MultipartFile multipartFile,
                                        @Validated @ModelAttribute(name = "datosPelicula") Pelicula pelicula,
                                        BindingResult bindingResult,
                                        RedirectAttributes attributes, Model model,
                                        @RequestParam("page") Optional<Integer> page, HttpSession session,
                                        @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);




        final String FILE_PATH_ROOT = "D:/ficheros";
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        pelicula.setUsuarioPelicula(user.get());

        if (bindingResult.hasErrors()){
            model.addAttribute("peliculaActual", pelicula.getId());
            return "peliculas";
        }else {
            Optional<Pelicula> pelicula2 = peliculaService.getPeliculaByTituloAndUsuario(pelicula.getTitulo(),user.get());
            if (pelicula2.isPresent()){
                if (!Objects.equals(pelicula.getId(), pelicula2.get().getId())){
                    model.addAttribute("tituloRepetido2", "Ya tienes una película con el título: " + pelicula.getTitulo());
                    model.addAttribute("peliculaRepetida", pelicula.getId());
                    return "peliculas";
                }
            }
        }
            try {
                if (multipartFile.isEmpty()){

                    Boolean archivo = Files.exists(Path.of(FILE_PATH_ROOT+"/" + ( "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId()  + ".jpg")));

                    if (archivo.equals(true)){
                        pelicula.setImagenRuta("/imagenes/leerImagen/" + "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId()  + ".jpg");
                    }else {
                        pelicula.setImagenRuta("/imagenes/leerImagen/" + "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId()  + ".png");
                    }

                } else{
                    //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por String Pelicula el id del objeto pelicula el titulo del objeto pelicula y la extensión del archivo(jpg, png)
                    String nombreArchivo = "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                    //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)

                    if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId() + ".jpg")))) {
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId() +".jpg"));
                    } else{
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Pelicula" + pelicula.getId() + "Usuario" + pelicula.getUsuarioPelicula().getId() +".png"));

                    }
                    fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);

                    //Modificamos el nombre del atributo imagenRuta del objeto pelicula con la url que genera el controlador ImagenesController
                    pelicula.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);

                }

                //Volvemos a guardar el objeto en la BBDD con los cambios
                peliculaService.saveEntity(pelicula);
                attributes.addFlashAttribute("success","Elemento añadido correctamente");
            } catch (DataIntegrityViolationException e){
                logger.error("Error al guardar la pelicula modificada por nombres duplicados");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar la pelicula modificada");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/peliculas";


    }


    //Eliminar Pelicula
    @PostMapping("/deletePelicula")
    public String deletePelicula(@RequestParam("id") Integer id, RedirectAttributes attributes){
        final String FILE_PATH_ROOT = "D:/ficheros";
        try {
            Optional<Pelicula> peliculaEliminar = peliculaService.getById(id);
            if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Pelicula" + peliculaEliminar.get().getId() + "Usuario" + peliculaEliminar.get().getUsuarioPelicula().getId() + ".jpg")))) {
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Pelicula" + peliculaEliminar.get().getId() + "Usuario" + peliculaEliminar.get().getUsuarioPelicula().getId() +".jpg"));
            } else{
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Pelicula" + peliculaEliminar.get().getId() + "Usuario" + peliculaEliminar.get().getUsuarioPelicula().getId() +".png"));

            }
            peliculaService.deleteEntity(peliculaEliminar.get());
            attributes.addFlashAttribute("success", "Película borrada");
        }catch (Exception e){
            logger.error("Error al eliminar la pelicula");
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "tituloPeliculaBusqueda", required = false) String tituloPeliculaBusqueda,
                         @RequestParam(value = "filtroPuntuacion", required = false) Integer filtroPuntuacion,
                         @RequestParam(value = "filtroGenero", required = false) Integer generoId,
                         @RequestParam(value = "filtroYear", required = false) Integer filtroYear,
                         @RequestParam(value = "filtroPlataforma", required = false) Integer plataformaId,
                         @RequestParam(value = "filtroOrden",required = false) String filtrOrden,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         HttpSession session, RedirectAttributes attributes) {
        Pelicula pelicula = new Pelicula();
        model.addAttribute("datosPelicula", pelicula);
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        pelicula.setUsuarioPelicula(user.get());

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
            Page<Pelicula> pagina = null;
            Optional<Plataforma> plataformaFiltro;
            Optional<GeneroElementoCompartido> generoFiltro;

            if(tituloPeliculaBusqueda == null || tituloPeliculaBusqueda.isBlank()){
                if (filtroPuntuacion!=null && generoId == null && filtroYear == null && plataformaId == null){
                    pagina = peliculaService.getAllPeliculasByPuntuacion(filtroPuntuacion, user.get(), pageRequest);
                    model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ninguna pelicula con esa puntuacion");
                        return "redirect:/peliculas";
                    }

                } else if (filtroPuntuacion == null && generoId != null && filtroYear == null && plataformaId == null) {
                    generoFiltro = generoElementoService.getById(generoId);
                    if (generoFiltro.isPresent()){
                        pagina = peliculaService.getAllPeliculasByGenero(generoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("generoFiltro", generoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna pelicula con ese género");
                            return "redirect:/peliculas";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "El genero no existe");
                        return "redirect:/peliculas";
                    }

                } else if (filtroPuntuacion == null && generoId == null && filtroYear != null && plataformaId == null) {
                    pagina = peliculaService.getAllPeliculasByYear(filtroYear, user.get(), pageRequest);
                    model.addAttribute("yearFiltro", filtroYear);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ninguna pelicula con ese año");
                        return "redirect:/peliculas";
                    }
                } else if (filtroPuntuacion == null && generoId == null && filtroYear == null && plataformaId != null) {
                    plataformaFiltro = plataformaService.getById(plataformaId);
                    if (plataformaFiltro.isPresent()){
                        pagina = peliculaService.getAllPeliculasByPlataforma(plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna pelicula con esa plataforma");
                            return "redirect:/peliculas";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "La plataforma no existe");
                        return "redirect:/peliculas";
                    }
                } else if (filtroPuntuacion != null && generoId != null && filtroYear != null && plataformaId != null) {
                    plataformaFiltro = plataformaService.getById(plataformaId);
                    generoFiltro = generoElementoService.getById(generoId);
                    if (plataformaFiltro.isPresent() && generoFiltro.isPresent()){
                        pagina = peliculaService.getAllPeliculasByAllFiltros(filtroPuntuacion, generoFiltro.get(),
                                filtroYear, plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                        model.addAttribute("generoFiltro", generoId);
                        model.addAttribute("yearFiltro", filtroYear);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna pelicula con esos filtros");
                            return "redirect:/peliculas";
                        }
                    }

                }else {
                    attributes.addFlashAttribute("failed", "Sólo se puede filtrar por título, género, año, valoración " +
                            "plataforma de forma individual o por género, año, valoración y plataforma juntos");
                    return "redirect:/peliculas";
                }
            }else {
                pagina = peliculaService.getAllPeliculasByTitulo(tituloPeliculaBusqueda, user.get(), pageRequest);
                if(pagina.getContent().isEmpty()){
                    attributes.addFlashAttribute("failed", "No hay películas con ese título");
                    return "redirect:/peliculas";
                }
                model.addAttribute("titulo", tituloPeliculaBusqueda);
            }


            // Agregar resultados al modelo
            model.addAttribute("pagina", pagina);
            assert pagina != null;
            int totalPages = pagina.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("size", pagina.getContent().size());
            model.addAttribute("peliculas", pagina.getContent());
            model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
            model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        }catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");
        }
        return "peliculas";
    }

    private void paginacion(Model model, Optional<Integer> page, HttpSession session, String orden){
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));

        calcularAniosUsuario(model, user);


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
        Page<Pelicula> pagina = peliculaService.getAllPeliculas(user.get(), pageRequest);

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
        model.addAttribute("peliculas", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

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