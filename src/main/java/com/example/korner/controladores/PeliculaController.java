package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.repositorios.PeliculaRepository;
import com.example.korner.servicio.FileSystemStorageService;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import com.example.korner.servicio.PeliculaServiceImpl;
import com.example.korner.servicio.PlataformaServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    PeliculaServiceImpl peliculaService;
    GeneroElementoServiceImpl generoElementoService;

    PlataformaServiceImpl plataformaService;
    FileSystemStorageService fileSystemStorageService;

    public PeliculaController(PeliculaServiceImpl peliculaService, GeneroElementoServiceImpl generoElementoService,
                              FileSystemStorageService fileSystemStorageService,PlataformaServiceImpl plataformaService) {
        this.peliculaService = peliculaService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.plataformaService = plataformaService;
    }

    private final Logger logger = LoggerFactory.getLogger(PeliculaController.class);

    //Mostrar Peliculas
    @GetMapping("")
    public String listAllPeliculas(Model model){
        List<Pelicula> listadoPeliculas = peliculaService.getAll();
        Pelicula pelicula = new Pelicula();
        List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
        List<Plataforma> plataformasList = plataformaService.getAll();
        model.addAttribute("listaGeneros", generoElementoCompartidoList);
        model.addAttribute("listaPlataformas", plataformasList);
        model.addAttribute("size", listadoPeliculas.size());
        model.addAttribute("peliculas", listadoPeliculas);
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
                               BindingResult bindingResult, RedirectAttributes attributes,Model model){
        if (bindingResult.hasErrors() || multipartFile.isEmpty()){
            if (multipartFile.isEmpty()){
                ObjectError error = new ObjectError("imagenError", "Debes seleccionar una imagen");
                bindingResult.addError(error);
                attributes.addFlashAttribute("failed", "Error al introducir la imagen, debe seleccionar una");
                List<GeneroElementoCompartido> generoElementoCompartidoList = generoElementoService.getAll();
                model.addAttribute("listaGeneros", generoElementoCompartidoList);
                return "peliculas";
            }
            attributes.addFlashAttribute("failed", "Error al introducir los datos en el formulario");
            return "peliculas";

        }else {
            try {
                logger.info("este es el objeto pelicula recibido{}", pelicula);
            /*guardamos en la BBDD  el objeto pelicula con el resto de la información que hemos obtenido
             del formulario para que genere un id al guardarse
             */
                peliculaService.saveEntity(pelicula);
                logger.info("este es el objeto pelicula guardado{}", pelicula);
            /*Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id
             del objeto pelicula y la extensión del archivo(jpg, png)
             */
                String nombreArchivo = pelicula.getId() + pelicula.getTitulo() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
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


}
