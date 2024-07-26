package com.example.korner.controladores;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/imagenes")

//A través del controlador generamos la url para visualizar la imagen la cual es /imagenes/leerImagen/y el nombre de la imagen

public class ImagenesController {
    /*
    Metodo por el cual obtenemos la ruta donde está almacenada la imagen y la transformamos a bytes para poder
    visualizarla
     */
    @GetMapping(value = "/leerImagen/{id}") // id es el nombre que tiene la imagen
    public ResponseEntity <byte[]> leerImagen(@PathVariable("id") String id) {
        final String FILE_PATH_ROOT = "D:/ficheros";
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File(FILE_PATH_ROOT+ "/"+ id));

        }catch (IOException e) {

            e.printStackTrace();

        }
        if(FilenameUtils.getExtension(id).equals("jpg")){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        }else {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);

        }
//
    }


}
