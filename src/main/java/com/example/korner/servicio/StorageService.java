package com.example.korner.servicio;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

/*
Para que funcione hay que añadir en el pom la sieguiente dependencia
        <dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.16.1</version>
		</dependency>
 */

/**
 * Esta interfaz proporciona los métodos esenciales que cualquier servicio de almacenamiento debe implementar para
 * manejar archivos dentro de una aplicación. Permite inicializar el sistema de almacenamiento, almacenar nuevos
 * archivos, cargar archivos individuales o todos los archivos, y borrar todos los datos almacenados.
 */
public interface StorageService {
    void init();
    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
