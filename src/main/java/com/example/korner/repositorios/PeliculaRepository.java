package com.example.korner.repositorios;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Pelicula.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    /**
     * Encuentra todas las películas cuyo título contenga el texto especificado (titulo), ignorando mayúsculas y minúsculas,
     * y que pertenezcan al usuario dado.
     * @param titulo Texto que debe estar contenido en el título dla película.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */
    Page<Pelicula> findAllByTituloContainingIgnoreCaseAndUsuarioPelicula(String titulo, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas que pertenecen a un usuario específico (user).
     * @param user El usuario al que pertenecen las películas.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula>findAllByUsuarioPelicula(Usuario user, Pageable pageable);

    /**
     * Encuentra todas las películas con una puntuación específica (puntuacion) que pertenecen a un usuario dado (usuario).
     * @param puntuacion La puntuación de la película.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByPuntuacionAndUsuarioPelicula(Integer puntuacion, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas que pertenecen a un género específico (genero) y al usuario dado (usuario).
     * @param genero El género de la película.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByGenerosPeliculaAndUsuarioPelicula(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas por año de visualización específico (year) que pertenecen a un usuario dado (usuario).
     * @param year El año de visualización de la película.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByYearAndUsuarioPelicula(Integer year, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas por la plataforma en la que lo han visto (plataforma) y pertenecen a un usuario dado (usuario).
     * @param plataforma La plataforma en la que han visto la película.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByPlataformasPeliculaAndUsuarioPelicula(Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas que cumplen con una puntuación específica (puntuacion), pertenecen a un género específico (genero),
     * han sido vistos en el año especificado (year), han sido vistos en una plataforma específica (plataforma), y pertenecen a un usuario dado (usuario).
     * @param puntuacion  La puntuación de la película.
     * @param genero El género de la película.
     * @param year El año de visualización de la película
     * @param plataforma La plataforma de visualización de la película
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByPuntuacionAndGenerosPeliculaAndYearAndPlataformasPeliculaAndUsuarioPelicula(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las películas cuyos ID están en una lista específica (ids).
     * @param ids Lista de identificadores de las películas.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByIdIn(List<Integer> ids, Pageable pageable);

    /**
     * Encuentra todas las películas cuyos ID están en una lista específica (ids) y que pertenecen a un usuario específico (usuario).
     * @param ids Lista de identificadores de las películas.
     * @param usuario El usuario al que pertenece la película.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Película que cumplen con los criterios de búsqueda.
     */

    Page<Pelicula> findAllByIdInAndUsuarioPelicula(List<Integer> ids,Usuario usuario, Pageable pageable);

    /**
     * Encuentra una película con un título específico (titulo) que pertenece a un usuario dado (usuario).
     * @param titulo El título dla película.
     * @param usuario El usuario al que pertenece la película.
     * @return Un objeto Película específico que cumple con los criterios, null si no encuentra nada
     */

    Optional<Pelicula> findPeliculaByTituloAndUsuarioPelicula(String titulo, Usuario usuario);


}
