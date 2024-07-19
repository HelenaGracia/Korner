package com.example.korner.controladores;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.repositorios.PeliculaRepository;
import com.example.korner.servicio.FileSystemStorageService;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import com.example.korner.servicio.PeliculaServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    PeliculaServiceImpl peliculaService;
    GeneroElementoServiceImpl generoElementoService;
    FileSystemStorageService fileSystemStorageService;

    public PeliculaController(PeliculaServiceImpl peliculaService, GeneroElementoServiceImpl generoElementoService, FileSystemStorageService fileSystemStorageService) {
        this.peliculaService = peliculaService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    private final Logger logger = LoggerFactory.getLogger(PeliculaController.class);


    //Mostrar Peliculas

    @GetMapping("")
    public String listAllPeliculas(Model model){
        model.addAttribute("peliculas", peliculaService.getAll());
        return "peliculas";
    }


    //Guardar Pelicula

    @PostMapping("/save")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String save(@RequestParam("imagen") MultipartFile multipartFile, Pelicula pelicula){
        logger.info("este es el objeto pelicula guardado{}", pelicula);
        //guardamos en la BBDD  el objeto pelicula con el resto de la información que hemos obtenido del formulario para que genere un id al guardarse
        peliculaService.saveEntity(pelicula);
        logger.info("este es el objeto pelicula guardado{}", pelicula);
        //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id del objeto pelicula y la extensión del archivo(jpg, png)
        String nombreArchivo = pelicula.getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
        fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
        //Modificamos el nombre del atributo imagenRuta del objeto pelicula con la url que genera el controlador ImagenesController
        pelicula.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
        //Volvemos a guardar el objeto en la BBDD con los cambios

        peliculaService.saveEntity(pelicula);
        return "redirect:/peliculas";
    }


    //Eliminar Pelicula
    @PostMapping("/delete")
    public String delete(Pelicula pelicula){
        logger.info("este es el objeto pelicula eliminado{}", pelicula);

        peliculaService.deleteEntity(pelicula);
        return "redirect:/peliculas";
    }

}
