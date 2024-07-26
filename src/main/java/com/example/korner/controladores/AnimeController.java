package com.example.korner.controladores;


import com.example.korner.modelo.Animes;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.servicio.AnimeServiceImpl;
import com.example.korner.servicio.FileSystemStorageService;
import com.example.korner.servicio.GeneroElementoServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/animes")
public class AnimeController {


    //En vez de autowired, inyectamos las dependencias mediante el constructor https://platzi.com/discusiones/2317-spring-boot/168849-que-diferencia-hay-entre-crear-el-constructor-y-utilizar-la-anotacion-autowired/
    private final AnimeServiceImpl animeService;
    private final GeneroElementoServiceImpl generoElementoService;
    private final FileSystemStorageService fileSystemStorageService;



    public AnimeController(AnimeServiceImpl animeService, GeneroElementoServiceImpl generoElementoService, FileSystemStorageService fileSystemStorageService) {
        this.animeService = animeService;
        this.generoElementoService = generoElementoService;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @GetMapping
    public String showAnime(Model model, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {

        //Paginacion
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        PageRequest pageRequest = PageRequest.of(currentPage-1, pageSize);
        Page<Animes> pagina = animeService.findAll(pageRequest);
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        List<Animes> listaAnime = animeService.getAll();
        List<GeneroElementoCompartido> listaGeneroElemento = generoElementoService.getAll();
        Animes anime = new Animes();
        model.addAttribute("datosAnime", anime);
        model.addAttribute("listaGeneros", listaGeneroElemento);
        model.addAttribute("size", listaAnime.size());
        model.addAttribute("animes", listaAnime);
        return "animes";
    }



    @PostMapping("/saveAnime")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String save(@RequestParam("imagen") MultipartFile multipartFile, Animes anime, RedirectAttributes attributes){

        try {
            //guardamos en la BBDD  el objeto anime con el resto de la información que hemos obtenido del formulario para que genere un id al guardarse
            animeService.saveEntity(anime);
            //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id del objeto anime y la extensión del archivo(jpg, png)
            String nombreArchivo = anime.getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)
            fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
            //Modificamos el nombre del atributo imagenRuta del objeto anime con la url que genera el controlador ImagenesController
            anime.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);
            //Volvemos a guardar el objeto en la BBDD con los cambios
            animeService.saveEntity(anime);
            attributes.addFlashAttribute("success","Elemento añadido correctamente");
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/animes";

    }

    @PostMapping("/saveModificar")
    //Obtenemos del formulario el contenido del input imagen, que es un archivo de imagen y se lo pasamos al parametro multipartFile
    public String saveModificar(@RequestParam("imagen") MultipartFile multipartFile, Animes anime, RedirectAttributes attributes){
        final String FILE_PATH_ROOT = "C:/ficheros";
        try {
            if (multipartFile.isEmpty()){


                    Boolean archivo = Files.exists(Path.of(FILE_PATH_ROOT+"/" + (anime.getId() + ".jpg")));

                    if (archivo.equals(true)){
                        anime.setImagenRuta("/imagenes/leerImagen/" + anime.getId() + ".jpg");
                    }else {
                        anime.setImagenRuta("/imagenes/leerImagen/" + anime.getId() + ".png");
                    }

                } else{
                //Creamos nuestros proprios nombres que van a llevar los archivos de imagenes, compuestos por el id del objeto anime y la extensión del archivo(jpg, png)
                String nombreArchivo = anime.getId() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                //Llamamos al metedos y le pasamos los siguientes argumentos(el archivo de imagen, nombre de la imagen)

                if(Files.exists(Path.of(FILE_PATH_ROOT+"/" + (anime.getId() + ".jpg")))) {
                    FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ anime.getId()+".jpg"));
                } else{
                    FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ anime.getId()+".png"));

                }
                fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);

                //Modificamos el nombre del atributo imagenRuta del objeto anime con la url que genera el controlador ImagenesController
                anime.setImagenRuta("/imagenes/leerImagen/" + nombreArchivo);
                //Volvemos a guardar el objeto en la BBDD con los cambios
            }


            animeService.saveEntity(anime);
            attributes.addFlashAttribute("success","Elemento añadido correctamente");
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error debido a nombres duplicados");
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/animes";

    }


    @PostMapping("/deleteAnime")
    public String delete(Animes anime,RedirectAttributes attributes){
        try {
            animeService.deleteEntity(anime);
            attributes.addFlashAttribute("success", "Elemento borrado");
        }catch (Exception e){
            attributes.addFlashAttribute("failed", "Error al eliminar");
        }
        return "redirect:/animes";
    }




}
