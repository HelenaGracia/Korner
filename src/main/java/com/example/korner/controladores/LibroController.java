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
@RequestMapping("/libros")
public class LibroController {

    private final LibroServiceImpl libroService;

    private final GeneroElementoServiceImpl generoElementoService;

    private final FormatoLibroServiceImpl formatoLibroService;
    private final FileSystemStorageService fileSystemStorageService;

    private final UsuarioSecurityService usuarioSecurityService;



    public LibroController(LibroServiceImpl libroService,
                           GeneroElementoServiceImpl generoElementoService, FormatoLibroServiceImpl formatoLibroService,
                           FileSystemStorageService fileSystemStorageService,
                           UsuarioSecurityService usuarioSecurityService) {
        this.libroService = libroService;
        this.generoElementoService = generoElementoService;
        this.formatoLibroService = formatoLibroService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    private final Logger logger = LoggerFactory.getLogger(LibroController.class);

    //Mostrar Libros
    @GetMapping("")
    public String listAllLibros(Model model, @RequestParam("page") Optional<Integer> page,
                                   HttpSession session, @RequestParam(value = "orden", required = false) String orden){


        paginacion(model, page, session, orden);



        Libro libro = new Libro();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<FormatoLibro> formatosList = formatoLibroService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaFormatos", formatosList);
        model.addAttribute("datosLibro", libro);

        return "libros";
    }


    //Guardar Libro

    @PostMapping("/saveLibro")
    /*Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y
      se lo pasamos al parametro multipartFile
     */
    public String saveLibro(@RequestParam("imagen") MultipartFile multipartFile,
                               @Validated @ModelAttribute(name = "datosLibro") Libro libro,
                               BindingResult bindingResult, RedirectAttributes attributes,Model model,
                               @RequestParam("page") Optional<Integer> page, HttpSession session,
                               @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);



        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<FormatoLibro> formatosList = formatoLibroService.getAll();
        model.addAttribute("listaFormatos", formatosList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        if (bindingResult.hasErrors() || multipartFile.isEmpty()){
            if (multipartFile.isEmpty()){
                ObjectError error = new ObjectError("imagenError", "Debes seleccionar una imagen");
                bindingResult.addError(error);
                attributes.addFlashAttribute("failed", "Error al introducir la imagen, debe seleccionar una");
                model.addAttribute("libroActual", -1);
                return "libros";
            }
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            model.addAttribute("libroActual", -1);
            return "libros";

        } else if (libroService.getLibroByTituloAndUsuario(libro.getTitulo(), user.get()).isPresent()) {
            model.addAttribute("tituloRepetido", "Ya tienes un libro con ese título");
            return "libros";
        } else {
            try {
                libro.setUsuarioLibro(user.get());

            /*guardamos en la BBDD  el objeto libro con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                libroService.saveEntity(libro);

            /*Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id
             del objeto libro y la extensión del archivo(jpg, png)
             */
                String nombreArchivo = "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metodo y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                //Modificamos el nombre del atributo imagenRuta del objeto libro con la url que genera el controlador ImagenesController
                libro.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
                //Volvemos a guardar el objeto en la BBDD con los cambios
                libroService.saveEntity(libro);
                attributes.addFlashAttribute("success", "Libro añadido correctamente");
            }catch (DataIntegrityViolationException e){
                logger.error("Error al guardar el libro por nombres duplicados");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar el libro");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/libros";
        }

    }


    @PostMapping("/saveLibroModificar")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String saveLibroModificar(@RequestParam("imagen") MultipartFile multipartFile,
                                        @Validated @ModelAttribute(name = "datosLibro") Libro libro,
                                        BindingResult bindingResult,
                                        RedirectAttributes attributes, Model model,
                                        @RequestParam("page") Optional<Integer> page, HttpSession session,
                                        @RequestParam(value = "orden", required = false) String orden){

        paginacion(model, page, session, orden);




        final String FILE_PATH_ROOT = "D:/ficheros";
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<FormatoLibro> formatosList = formatoLibroService.getAll();
        model.addAttribute("listaFormatos", formatosList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        libro.setUsuarioLibro(user.get());

        if (bindingResult.hasErrors()){
            model.addAttribute("libroActual", libro.getId());
            return "libros";
        }else {
            Optional<Libro> libro2 = libroService.getLibroByTituloAndUsuario(libro.getTitulo(), user.get());
            if (libro2.isPresent()){
                if (!Objects.equals(libro.getId(), libro2.get().getId())){
                    model.addAttribute("tituloRepetido2", "Ya tienes un libro con el título: " + libro.getTitulo());
                    model.addAttribute("libroRepetido", libro.getId());
                    return "libros";
                }
            }
        }
            try {
                if (multipartFile.isEmpty()){

                    Boolean archivo = Files.exists(Path.of(FILE_PATH_ROOT+"/" + ( "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId()  + ".jpg")));

                    if (archivo.equals(true)){
                        libro.setImagenRuta("/imagenes/leerImagen/" + "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId()  + ".jpg");
                    }else {
                        libro.setImagenRuta("/imagenes/leerImagen/" + "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId()  + ".png");
                    }

                } else{
                    //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por String Libro el id del objeto libro el titulo del objeto libro y la extensión del archivo(jpg, png)
                    String nombreArchivo = "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                    //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)

                    if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId() + ".jpg")))) {
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId() +".jpg"));
                    } else{
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Libro" + libro.getId() + "Usuario" + libro.getUsuarioLibro().getId() +".png"));

                    }
                    fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);

                    //Modificamos el nombre del atributo imagenRuta del objeto libro con la url que genera el controlador ImagenesController
                    libro.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);

                }

                //Volvemos a guardar el objeto en la BBDD con los cambios
                libroService.saveEntity(libro);
                attributes.addFlashAttribute("success","Libro modificado correctamente");
            } catch (DataIntegrityViolationException e){
                logger.error("Error al guardar el libro modificado por nombres duplicados");
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                logger.error("Error al guardar el libro modificado");
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/libros";


    }


    //Eliminar Libro
    @PostMapping("/deleteLibro")
    public String deleteLibro(@RequestParam("id") Integer id, RedirectAttributes attributes){
        final String FILE_PATH_ROOT = "D:/ficheros";
        try {
            Optional<Libro>eliminarLibro = libroService.getById(id);
            if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + ("Libro" + eliminarLibro.get().getId() + "Usuario" + eliminarLibro.get().getUsuarioLibro().getId() + ".jpg")))) {
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Libro" + eliminarLibro.get().getId() + "Usuario" + eliminarLibro.get().getUsuarioLibro().getId() +".jpg"));
            } else{
                FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ "Libro" + eliminarLibro.get().getId() + "Usuario" + eliminarLibro.get().getUsuarioLibro().getId() +".png"));
            }
            libroService.deleteEntity(eliminarLibro.get());
            attributes.addFlashAttribute("success", "Libro borrado");
        }catch (Exception e){
            logger.error("Error al eliminar el libro");
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }

        return "redirect:/libros";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "tituloLibroBusqueda", required = false) String tituloLibroBusqueda,
                         @RequestParam(value = "filtroPuntuacion", required = false) Integer filtroPuntuacion,
                         @RequestParam(value = "filtroGenero", required = false) Integer generoId,
                         @RequestParam(value = "filtroYear", required = false) Integer filtroYear,
                         @RequestParam(value = "filtroFormato", required = false) Integer formatoId,
                         @RequestParam(value = "filtroOrden",required = false) String filtrOrden,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         HttpSession session, RedirectAttributes attributes) {
        Libro libro = new Libro();
        model.addAttribute("datosLibro", libro);
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<FormatoLibro> formatosList = formatoLibroService.getAll();
        model.addAttribute("listaFormatos", formatosList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        libro.setUsuarioLibro(user.get());

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
            Page<Libro> pagina = null;
            Optional<FormatoLibro> formatoFiltro;
            Optional<GeneroElementoCompartido> generoFiltro;

            if(tituloLibroBusqueda == null || tituloLibroBusqueda.isBlank()){
                if (filtroPuntuacion!=null && generoId == null && filtroYear == null && formatoId == null){
                    pagina = libroService.getAllLibrosByPuntuacion(filtroPuntuacion, user.get(), pageRequest);
                    model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ningún libro con esa puntuación");
                        return "redirect:/libros";
                    }

                } else if (filtroPuntuacion == null && generoId != null && filtroYear == null && formatoId == null) {
                    generoFiltro = generoElementoService.getById(generoId);
                    if (generoFiltro.isPresent()){
                        pagina = libroService.getAllLibrosByGenero(generoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("generoFiltro", generoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún libro con ese género");
                            return "redirect:/libros";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "El genero no existe");
                        return "redirect:/libros";
                    }

                } else if (filtroPuntuacion == null && generoId == null && filtroYear != null && formatoId == null) {
                    pagina = libroService.getAllLibrosByYear(filtroYear, user.get(), pageRequest);
                    model.addAttribute("yearFiltro", filtroYear);
                    if (pagina.getContent().isEmpty()){
                        attributes.addFlashAttribute("failed", "No existe ningún libro con ese año");
                        return "redirect:/libros";
                    }
                } else if (filtroPuntuacion == null && generoId == null && filtroYear == null && formatoId != null) {
                    formatoFiltro = formatoLibroService.getById(formatoId);
                    if (formatoFiltro.isPresent()){
                        pagina = libroService.getAllLibrosByFormato(formatoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("formatoFiltro", formatoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún libro con ese formato");
                            return "redirect:/libros";
                        }
                    }else {
                        attributes.addFlashAttribute("failed", "El formato no existe");
                        return "redirect:/libros";
                    }
                } else if (filtroPuntuacion != null && generoId != null && filtroYear != null && formatoId != null) {
                    formatoFiltro = formatoLibroService.getById(formatoId);
                    generoFiltro = generoElementoService.getById(generoId);
                    if (formatoFiltro.isPresent() && generoFiltro.isPresent()){
                        pagina = libroService.getAllLibrosByAllFiltros(filtroPuntuacion, generoFiltro.get(),
                                filtroYear, formatoFiltro.get(), user.get(), pageRequest);
                        model.addAttribute("puntuacionFiltro", filtroPuntuacion);
                        model.addAttribute("generoFiltro", generoId);
                        model.addAttribute("yearFiltro", filtroYear);
                        model.addAttribute("formatoFiltro", formatoId);
                        if (pagina.getContent().isEmpty()){
                            attributes.addFlashAttribute("failed", "No existe ningún libro con esos filtros");
                            return "redirect:/libros";
                        }
                    }

                }else {
                    attributes.addFlashAttribute("failed", "Sólo se puede filtrar por título, género, año, valoración " +
                            "formato de forma individual o por género, año, valoración y formato juntos");
                    return "redirect:/libros";
                }
            }else {
                pagina = libroService.getAllLibrosByTitulo(tituloLibroBusqueda, user.get(), pageRequest);
                if(pagina.getContent().isEmpty()) {
                    attributes.addFlashAttribute("failed", "No hay libros con ese título");
                    return "redirect:/libros";
                }
                model.addAttribute("titulo", tituloLibroBusqueda);
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
            model.addAttribute("libros", pagina.getContent());
            model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
            model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        }catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");
        }
        return "libros";
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
         que le has dicho todos los objetos libro almacenados, es decir, crea la pagina que visualizas con el contenido
         */
        // Page<Libro> pagina = libroService.findAll(pageRequest);
        Page<Libro> pagina = libroService.getAllLibros(user.get(), pageRequest);

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
        model.addAttribute("libros", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

    }

    public void calcularAniosUsuario(Model model, Optional<Usuario> user) {
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