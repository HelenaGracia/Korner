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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/series")
public class SerieController {

    private final SerieServiceImpl serieService;
    private final GeneroElementoServiceImpl generoElementoService;
    private final PlataformaServiceImpl plataformaService;
    private final FileSystemStorageService fileSystemStorageService;
    private final UsuarioSecurityService usuarioSecurityService;

    private final Logger logger = LoggerFactory.getLogger(SerieController.class);

    public SerieController(SerieServiceImpl serieService, GeneroElementoServiceImpl generoElementoService, PlataformaServiceImpl plataformaService, FileSystemStorageService fileSystemStorageService, UsuarioSecurityService usuarioSecurityService) {
        this.serieService = serieService;
        this.generoElementoService = generoElementoService;
        this.plataformaService = plataformaService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    //Mostrar Series
    @GetMapping("")
    public String listAllSeries(Model model, @RequestParam("page") Optional<Integer> page,
                                   HttpSession session, @RequestParam(value = "orden", required = false) String orden){


        paginacion(model, page, session, orden);

        Serie serie = new Serie();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("datosSerie", serie);

        return "series";
    }

    //Guardar Serie
    @PostMapping("/saveSerie")
    /*Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y
      se lo pasamos al parametro multipartFile
     */
    public String saveSerie(@RequestParam("imagen") MultipartFile multipartFile,
                               @Validated @ModelAttribute(name = "datosSerie") Serie serie,
                               BindingResult bindingResult, RedirectAttributes attributes, Model model,
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
                model.addAttribute("serieActual", -1);
                return "series";
            }
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            model.addAttribute("serieActual", -1);
            return "series";

        } else if (serieService.getSerieByTituloAndUsuario(serie.getTitulo(),user.get()).isPresent()) {
            model.addAttribute("tituloRepetido", "Ya tienes una serie con ese título");
            return "series";
        } else {
            try {
                serie.setUsuarioSerie(user.get());

            /*guardamos en la BBDD  el objeto Serie con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                serieService.saveEntity(serie);

            /*Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id
             del objeto Serie y la extensión del archivo(jpg, png)
             */
                String nombreArchivo = "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metodo y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                //Modificamos el nombre del atributo imagenRuta del objeto Serie con la url que genera el controlador ImagenesController
                serie.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
                //Volvemos a guardar el objeto en la BBDD con los cambios
                serieService.saveEntity(serie);
                attributes.addFlashAttribute("success", "Elemento añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al guardar la serie creada por nombres duplicados");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar la serie creada");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/series";
        }
    }

    @PostMapping("/saveSerieModificar")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String saveSerieModificar(@RequestParam("imagen") MultipartFile multipartFile,
                                        @Validated @ModelAttribute(name = "datosSerie") Serie serie,
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

        serie.setUsuarioSerie(user.get());

        if (bindingResult.hasErrors()){
            model.addAttribute("serieActual", serie.getId());
            return "series";
        }else {
            Optional<Serie> serie2 = serieService.getSerieByTituloAndUsuario(serie.getTitulo(),user.get());
            if (serie2.isPresent()){
                if (!Objects.equals(serie.getId(), serie2.get().getId())){
                    model.addAttribute("tituloRepetido2", "Ya tienes una serie con el título: " + serie.getTitulo());
                    model.addAttribute("serieRepetida", serie.getId());
                    return "series";
                }
            }
        }
        try {
            if (multipartFile.isEmpty()){

                Boolean archivo = Files.exists(Path.of(FILE_PATH_ROOT+"/" + ( "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId()  + ".jpg")));

                if (archivo.equals(true)){
                    serie.setImagenRuta("/imagenes/leerImagen/" + "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId()  + ".jpg");
                }else {
                    serie.setImagenRuta("/imagenes/leerImagen/" + "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId()  + ".png");
                }

            } else{
                //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por String Serie el id del objeto Serie el titulo del objeto Serie y la extensión del archivo(jpg, png)
                String nombreArchivo = "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)

                if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId() + ".jpg")))) {
                    FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId() +".jpg"));
                } else{
                    FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Serie" + serie.getId() + "Usuario" + serie.getUsuarioSerie().getId() +".png"));

                }
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);

                //Modificamos el nombre del atributo imagenRuta del objeto Serie con la url que genera el controlador ImagenesController
                serie.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);

            }

            //Volvemos a guardar el objeto en la BBDD con los cambios
            serieService.saveEntity(serie);
            attributes.addFlashAttribute("success","Elemento añadido correctamente");
        } catch (DataIntegrityViolationException e){
            logger.error("Error al guardar la serie modificada por nombres duplicados");
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        } catch (Exception e){
            logger.error("Error al guardar la serie modificada");
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/series";
    }

    //Eliminar Serie
    @PostMapping("/deleteSerie")
    public String deleteSerie( @RequestParam("id") Integer id, RedirectAttributes attributes){
        final String FILE_PATH_ROOT = "D:/ficheros";
        try {
            Optional<Serie> serieEliminar = serieService.getById(id);
            if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Serie" + serieEliminar.get().getId() + "Usuario" + serieEliminar.get().getUsuarioSerie().getId() + ".jpg")))) {
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Serie" + serieEliminar.get().getId() + "Usuario" + serieEliminar.get().getUsuarioSerie().getId() +".jpg"));
            } else{
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Serie" + serieEliminar.get().getId() + "Usuario" + serieEliminar.get().getUsuarioSerie().getId() +".png"));

            }
            serieService.deleteEntity(serieEliminar.get());
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            logger.error("Error al elminar la serie");
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }

        return "redirect:/series";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "tituloSerieBusqueda", required = false) String tituloSerieBusqueda,
                         @RequestParam(value = "filtroPuntuacion", required = false) Integer filtroPuntuacion,
                         @RequestParam(value = "filtroGenero", required = false) Integer generoId,
                         @RequestParam(value = "filtroYear", required = false) Integer filtroYear,
                         @RequestParam(value = "filtroPlataforma", required = false) Integer plataformaId,
                         @RequestParam(value = "filtroOrden",required = false) String filtrOrden,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         HttpSession session, RedirectAttributes attributes) {
        Serie serie = new Serie();
        model.addAttribute("datosSerie", serie);
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        serie.setUsuarioSerie(user.get());

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
            Page<Serie> pagina = null;
            Optional<Plataforma> plataformaFiltro;
            Optional<GeneroElementoCompartido> generoFiltro;

            if(tituloSerieBusqueda == null || tituloSerieBusqueda.isBlank()){
                if (filtroPuntuacion!=null && generoId == null && filtroYear == null && plataformaId == null){
                    pagina = serieService.getAllSeriesByPuntuacion(filtroPuntuacion, user.get(), pageRequest);
                    model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ninguna serie con esa puntuación");
                        return "redirect:/series";
                    }

                } else if (filtroPuntuacion == null && generoId != null && filtroYear == null && plataformaId == null) {
                    generoFiltro = generoElementoService.getById(generoId);
                    if (generoFiltro.isPresent()){
                        pagina = serieService.getAllSeriesByGenero(generoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("generoFiltro", generoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna serie con ese género");
                            return "redirect:/series";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "El genero no existe");
                        return "redirect:/series";
                    }

                } else if (filtroPuntuacion == null && generoId == null && filtroYear != null && plataformaId == null) {
                    pagina = serieService.getAllSeriesByYear(filtroYear, user.get(), pageRequest);
                    model.addAttribute("yearFiltro", filtroYear);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ninguna serie con ese año");
                        return "redirect:/series";
                    }
                } else if (filtroPuntuacion == null && generoId == null && filtroYear == null && plataformaId != null) {
                    plataformaFiltro = plataformaService.getById(plataformaId);
                    if (plataformaFiltro.isPresent()){
                        pagina = serieService.getAllSeriesByPlataforma(plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna serie con esa plataforma");
                            return "redirect:/series";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "La plataforma no existe");
                        return "redirect:/series";
                    }
                } else if (filtroPuntuacion != null && generoId != null && filtroYear != null && plataformaId != null) {
                    plataformaFiltro = plataformaService.getById(plataformaId);
                    generoFiltro = generoElementoService.getById(generoId);
                    if (plataformaFiltro.isPresent() && generoFiltro.isPresent()){
                        pagina = serieService.getAllSeriesByAllFiltros(filtroPuntuacion, generoFiltro.get(),
                                filtroYear, plataformaFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                        model.addAttribute("generoFiltro", generoId);
                        model.addAttribute("yearFiltro", filtroYear);
                        model.addAttribute("plataformaFiltro", plataformaId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ninguna serie con esos filtros");
                            return "redirect:/series";
                        }
                    }

                }else {
                    attributes.addFlashAttribute("failed", "Sólo se puede filtrar por título, género, año, valoración " +
                            "plataforma de forma individual o por género, año, valoración y plataforma juntos");
                    return "redirect:/series";
                }
            }else {
                pagina = serieService.getAllSeriesByTitulo(tituloSerieBusqueda, user.get(), pageRequest);
                if(pagina.getContent().isEmpty()){
                    attributes.addFlashAttribute("failed", "No hay series con ese título");
                    return "redirect:/series";
                }
                model.addAttribute("titulo", tituloSerieBusqueda);
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
            model.addAttribute("series", pagina.getContent());
            model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
            model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        }catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");
        }
        return "series";
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
         que le has dicho todos los objetos Serie almacenados, es decir, crea la pagina que visualizas con el contenido
         */
        Page<Serie> pagina = serieService.getAllSeries(user.get(), pageRequest);

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
        model.addAttribute("series", pagina.getContent());
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
