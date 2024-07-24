package com.example.korner.controladores;


import com.example.korner.modelo.Libro;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.servicio.LibroServiceImpl;
import com.example.korner.servicio.FileSystemStorageService;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {


    //En vez de autowired, inyectamos las dependencias mediante el constructor https://platzi.com/discusiones/2317-spring-boot/168849-que-diferencia-hay-entre-crear-el-constructor-y-utilizar-la-anotacion-autowired/
    private final LibroServiceImpl libroService;
    private final GeneroElementoServiceImpl generoElementoService;
    private final FileSystemStorageService fileSystemStorageService;



    public LibroController(LibroServiceImpl libroService, GeneroElementoServiceImpl generoElementoService, FileSystemStorageService fileSystemStorageService) {
        this.libroService = libroService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @GetMapping
    public String showLibro(Model model) {
        List<Libro> listaLibro = libroService.getAll();
        List<GeneroElementoCompartido> listaGeneroElemento = generoElementoService.getAll();
        Libro libro = new Libro();
        model.addAttribute("datosLibro", libro);
        model.addAttribute("listaGeneros", listaGeneroElemento);
        model.addAttribute("size", listaLibro.size());
        model.addAttribute("libros", listaLibro);
        return "libros";
    }



    @PostMapping("/saveLibro")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String save(@RequestParam("imagen") MultipartFile multipartFile, Libro libro, RedirectAttributes attributes){

        try {
            //guardamos en la BBDD  el objeto libro con el resto de la información que hemos obtenido del formulario para que genere un id al guardarse
            libroService.saveEntity(libro);
            //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id del objeto libro y la extensión del archivo(jpg, png)
            String nombreArchivo = libro.getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
            fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
            //Modificamos el nombre del atributo imagenRuta del objeto anime con la url que genera el controlador ImagenesController
            libro.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);
            //Volvemos a guardar el objeto en la BBDD con los cambios
            libroService.saveEntity(libro);
            attributes.addFlashAttribute("success","Elemento añadido correctamente");
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error al guardar el libro");
        }
        return "redirect:/libros";

    }


    @PostMapping("/deleteLibro")
    public String delete(Libro libro,RedirectAttributes attributes){
        try {
            libroService.deleteEntity(libro);
            attributes.addFlashAttribute("success", "Elemento borrado correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar el libro");
        }
        return "redirect:/libros";
    }

}
