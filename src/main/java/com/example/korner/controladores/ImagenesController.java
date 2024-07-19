package com.example.korner.controladores;

import com.example.korner.servicio.FileSystemStorageService;

import org.apache.catalina.util.IOTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/imagenes")
public class ImagenesController {


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
//
    }
}
