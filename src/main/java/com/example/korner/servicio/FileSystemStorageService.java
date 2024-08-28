package com.example.korner.servicio;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/*
Para que funcione hay que añadir en el pom la sieguiente dependencia
        <dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.16.1</version>
		</dependency>
 */

/**
 * Es una clase de servicio que proporciona una implementación completa para gestionar el almacenamiento de archivos
 * en un sistema de archivos local. Permite:
 * - Guardar archivos: ya sea con su nombre original o un nombre personalizado.
 * - Listar archivos: cargar todos los archivos disponibles o un archivo específico.
 * - Borrar archivos: limpiar todos los archivos del directorio de almacenamiento.
 * - Inicializar almacenamiento: asegurarse de que el directorio de almacenamiento esté preparado para el uso.
 */
@Service
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation;

    /**
     * Creación de la carpeta ficheros en la cual se van a guardar las imagenes que los usuarios suben a la aplicación
     * la carpeta se está generando en la raiz del disco D (en el ordenador en el que se está creando la apliacion),
     * depende de que ubicacion considera rootLocation en cada ordenador
     */
    public FileSystemStorageService() {
        this.rootLocation = Paths.get("/ficheros");
        if (Files.notExists(this.rootLocation)) {
            try {
                Files.createDirectories(this.rootLocation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Este método se encarga de almacenar un archivo en la carpeta
     * @param file representa el archivo que se va a almacenar
     */
    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * Este método se encarga de almacenar un archivo en la carpeta pero permite especificar un nombre de
     * archivo personalizado (nombreAchivo) para almacenar el archivo.
     * @param file  representa el archivo que se va a almacenar
     * @param nombreAchivo Cadena de texto que representa el nombre del archivo
     */

    public void storeWithName(MultipartFile file, String nombreAchivo) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(nombreAchivo))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * Este método devuelve una lista (Stream<Path>) de todos los archivos almacenados en el directorio rootLocation, excluyendo la carpeta raíz.
     * @return devuelve una lista (Stream<Path>)
     */

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }

    }

    /**
     * Resuelve y devuelve la ruta completa de un archivo específico en rootLocation usando su nombre (filename).
     * @param filename Cadena de texto que representa el nombre del archivo
     * @return Devuelve la ruta completa del archivo
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Este método carga un archivo como un recurso (Resource). Verifica si el archivo existe y es legible antes de devolverlo como un UrlResource.
     * @param filename Cadena de texto que representa el nombre del archivo
     * @return Devuleve el archivo como UrlResource
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    /**
     * Borra recursivamente todos los archivos y directorios en rootLocation
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Este método crea el directorio de almacenamiento (rootLocation) si no existe
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }
}
