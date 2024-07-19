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
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("")
    public String listAllPeliculas(Model model){
        model.addAttribute("peliculas", peliculaService.getAll());
        return "peliculas";
    }



    @PostMapping("/save")
    public String save(@RequestParam("imagen") MultipartFile multipartFile, Pelicula pelicula){
        logger.info("este es el objeto pelicula guardado{}", pelicula);
        peliculaService.saveEntity(pelicula);
        logger.info("este es el objeto pelicula guardado{}", pelicula);
        String nombreArchivo = pelicula.getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
        pelicula.setImagenRuta( "/imagenes/leerImagen/" + nombreArchivo);
        peliculaService.saveEntity(pelicula);
        return "redirect:/peliculas";
    }



    @PostMapping("/delete")
    public String delete(Pelicula pelicula){
        logger.info("este es el objeto pelicula eliminado{}", pelicula);
        peliculaService.deleteEntity(pelicula);
        return "redirect:/peliculas";
    }


}
