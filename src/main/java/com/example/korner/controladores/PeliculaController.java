package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.PeliculaRepository;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    private final PeliculaServiceImpl peliculaService;

    private final PeliculaRepository peliculaRepository;
    private final GeneroElementoServiceImpl generoElementoService;

    private final PlataformaServiceImpl plataformaService;
    private final FileSystemStorageService fileSystemStorageService;

    private final UsuarioSecurityService usuarioSecurityService;



    public PeliculaController(PeliculaServiceImpl peliculaService, PeliculaRepository peliculaRepository,
                              GeneroElementoServiceImpl generoElementoService,
                              FileSystemStorageService fileSystemStorageService,
                              PlataformaServiceImpl plataformaService, UsuarioSecurityService usuarioSecurityService) {
        this.peliculaService = peliculaService;
        this.peliculaRepository = peliculaRepository;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.plataformaService = plataformaService;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    private final Logger logger = LoggerFactory.getLogger(PeliculaController.class);

    //Mostrar Peliculas
    @GetMapping("")
    public String listAllPeliculas(Model model,
                                   @RequestParam("page") Optional<Integer> page,

                                   HttpSession session){




        paginacion(model, page, session);



        //List<Pelicula> listadoPeliculas = peliculaService.getAll();
        Pelicula pelicula = new Pelicula();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaPlataformas", plataformasList);
        //model.addAttribute("size", listadoPeliculas.size());

        //model.addAttribute("peliculas", listadoPeliculas);

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
                               @RequestParam("page") Optional<Integer> page, HttpSession session){

        paginacion(model, page, session);



        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        Set<GeneroElementoCompartido> listadoGeneros = pelicula.getGenerosPelicula();
        logger.info("listado de generos:{}", listadoGeneros);
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


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

        }else {
            try {
                Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

                pelicula.setUsuarioPelicula(user.get());
                logger.info("este es el objeto pelicula recibido{}", pelicula);
            /*guardamos en la BBDD  el objeto pelicula con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                peliculaService.saveEntity(pelicula);
                logger.info("este es el objeto pelicula guardado{}", pelicula);
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
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                e.printStackTrace();
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
                                        @RequestParam("page") Optional<Integer> page, HttpSession session){

        paginacion(model, page, session);




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
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
            } catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("failed", "Error");
            }
            return "redirect:/peliculas";
        }
        
    }


    //Eliminar Pelicula
    @PostMapping("/deletePelicula")
    public String deletePelicula(Pelicula pelicula, RedirectAttributes attributes){
        try {
            logger.info("este es el objeto pelicula eliminado{}", pelicula);
            peliculaService.deleteEntity(pelicula);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "tituloPeliculaBusqueda", required = false) String tituloPeliculaBusqueda,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         HttpSession session,
                         RedirectAttributes attributes) {
        logger.info("Titulo de la pelicula: {}", tituloPeliculaBusqueda);
        Pelicula pelicula = new Pelicula();
        model.addAttribute("datosPelicula", pelicula);
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("listaGeneros", generoElementoCompartidoList);


        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));

        pelicula.setUsuarioPelicula(user.get());

        try {
            // Determinar la página actual y configurar la paginación
            int currentPage = page.orElse(1);
            Pageable pageRequest = PageRequest.of(currentPage - 1, 2);

            // Determinar si se debe buscar por título o no
            Page<Pelicula> pagina;
            if (tituloPeliculaBusqueda == null) {
                pagina = peliculaService.findAll(pageRequest);
            } else {
                pagina = peliculaRepository.findAllByTituloContainingIgnoreCaseAndUsuarioPeliculaId(tituloPeliculaBusqueda,user.get().getId(),pageRequest);

                model.addAttribute("titulo", tituloPeliculaBusqueda);
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
            model.addAttribute("peliculas", pagina.getContent());

        } catch (Exception e) {
            logger.error("Error en la busqueda", e);  // Loguear el error
            attributes.addFlashAttribute("failed", "Error al realizar la busqueda");

        }

        return "peliculas";
    }

    private void paginacion(Model model, Optional<Integer> page, HttpSession session){
        Optional<Usuario> user = usuarioSecurityService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));



        //Recibe la pagina en la que estoy si no recibe nada asigna la pagina 1
        int currentPage = page.orElse(1);
        //Guarda la pagina en la que estoy (Si es la pagina 1, la 2...) y la cantidad de elementos que quiero mostrar en ella
        PageRequest pageRequest = PageRequest.of(currentPage-1, 2);
        /*
         se crea un objeto page que es el encargado de rellenar en la pagina que le has indicado con la cantidad
         que le has dicho todos los objetos pelicula almacenados, es decir, crea la pagina que visualizas con el contenido
         */
       // Page<Pelicula> pagina = peliculaService.findAll(pageRequest);
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






    }




}
