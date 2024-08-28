package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Libro.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    /**
     * Encuentra todos los libros cuyo título contenga el texto especificado (titulo), ignorando mayúsculas y minúsculas,
     * y que pertenezcan al usuario dado.
     * @param titulo Texto que debe estar contenido en el título del libro.
     * @param usuario  El usuario al que pertenece el libro.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByTituloContainingIgnoreCaseAndUsuarioLibro(String titulo, Usuario usuario, Pageable pageable);
    /**
     * Encuentra todos los libros que pertenecen a un usuario específico (user).
     * @param user El usuario al que pertenecen los libros.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro>findAllByUsuarioLibro(Usuario user, Pageable pageable);
    /**
     * Encuentra todos los libros con una puntuación específica (puntuacion) que pertenecen a un usuario dado (usuario).
     * @param puntuacion La puntuación del libro.
     * @param usuario  El usuario al que pertenece el libro.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByPuntuacionAndUsuarioLibro(Integer puntuacion, Usuario usuario, Pageable pageable);
    /**
     * Encuentra todos los libros que pertenecen a un género específico (genero) y al usuario dado (usuario).
     * @param genero El género del libro.
     * @param usuario  El usuario al que pertenece el libro.
     * @param pageable  Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByGenerosLibroAndUsuarioLibro(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los libros por año de visualización específico (year) que pertenecen a un usuario dado (usuario).
     * @param year El año de visualización del libro.
     * @param usuario El usuario al que pertenece el libro.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByYearAndUsuarioLibro(Integer year, Usuario usuario, Pageable pageable);
    /**
     * Encuentra todos los libros que pertenecen a un  formato específico (formato) y pertenecen a un usuario dado (usuario).
     * @param formato El formato al que pertence el libro.
     * @param usuario El usuario al que pertenece el libro.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByFormatosLibroAndUsuarioLibro(FormatoLibro formato, Usuario usuario, Pageable pageable);
    /**
     * Encuentra todos los libros que cumplen con una puntuación específica (puntuacion), pertenecen a un género específico (genero),
     * han sido vistos en el año especificado (year), pertencen a un formato específico (formato), y pertenecen a un usuario dado (usuario).
     * @param puntuacion  La puntuación del libro.
     * @param genero El género del libro.
     * @param year El año de visualización del libro
     * @param formato El formato al que pertence el libro.
     * @param usuario  El usuario al que pertenece el libro.
     * @param pageable  Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByPuntuacionAndGenerosLibroAndYearAndFormatosLibroAndUsuarioLibro(Integer puntuacion, GeneroElementoCompartido genero, Integer year, FormatoLibro formato, Usuario usuario, Pageable pageable);
    /**
     * Encuentra todos los libros cuyos ID están en una lista específica (ids).
     * @param ids Lista de identificadores de los libros.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByIdIn(List<Integer> ids, Pageable pageable);
    /**
     * Encuentra todos los libros cuyos ID están en una lista específica (ids) y que pertenecen a un usuario específico (usuario).
     * @param ids Lista de identificadores de los libros.
     * @param usuario El usuario al que pertenece el libro.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos libro que cumplen con los criterios de búsqueda.
     */
    Page<Libro> findAllByIdInAndUsuarioLibro(List<Integer> ids,Usuario usuario, Pageable pageable);
    /**
     * Encuentra un libro con un título específico (titulo) que pertenece a un usuario dado (usuario).
     * @param titulo El título del libro.
     * @param usuario El usuario al que pertenece el libro.
     * @return Un objeto libro específico que cumple con los criterios, null si no encuentra nada
     */
    Optional<Libro> findLibroByTituloAndUsuarioLibro(String titulo, Usuario usuario);

}
