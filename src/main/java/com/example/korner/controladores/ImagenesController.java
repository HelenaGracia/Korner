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
    /**
     * Este método se encarga de devolver una imagen almacenada en el servidor en formato binario
     * (como un arreglo de bytes) para que pueda ser mostrada en la página web o  o ser usada en una etiqueta HTML
     * @param id  es el nombre del archivo de imagen
     */
    @GetMapping(value = "/leerImagen/{id}")
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
    }


}
