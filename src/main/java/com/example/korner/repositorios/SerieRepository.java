package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Serie.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {
    /**
     * Encuentra todas las series cuyo título contenga el texto especificado (titulo), ignorando mayúsculas y minúsculas,
     * y que pertenezcan al usuario dado.
     * @param titulo Texto que debe estar contenido en el título dla serie.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */
    Page<Serie> findAllByTituloContainingIgnoreCaseAndUsuarioSerie(String titulo, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series que pertenecen a un usuario específico (user).
     * @param user El usuario al que pertenecen las series.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */
    Page<Serie>findAllByUsuarioSerie(Usuario user, Pageable pageable);

    /**
     * Encuentra todas las series con una puntuación específica (puntuacion) que pertenecen a un usuario dado (usuario).
     * @param puntuacion La puntuación de la serie.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */
    Page<Serie> findAllByPuntuacionAndUsuarioSerie(Integer puntuacion, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series que pertenecen a un género específico (genero) y al usuario dado (usuario).
     * @param genero El género de la serie.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByGenerosSerieAndUsuarioSerie(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series por año de visualización específico (year) que pertenecen a un usuario dado (usuario).
     * @param year El año de visualización de la serie.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByYearAndUsuarioSerie(Integer year, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series por la plataforma en la que lo han visto (plataforma) y pertenecen a un usuario dado (usuario).
     * @param plataforma La plataforma en la que han visto la serie.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByPlataformasSerieAndUsuarioSerie(Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series que cumplen con una puntuación específica (puntuacion), pertenecen a un género específico (genero),
     * han sido vistos en el año especificado (year), han sido vistos en una plataforma específica (plataforma), y pertenecen a un usuario dado (usuario).
     * @param puntuacion  La puntuación de la serie.
     * @param genero El género de la serie.
     * @param year El año de visualización de la serie
     * @param plataforma La plataforma de visualización de la serie
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByPuntuacionAndGenerosSerieAndYearAndPlataformasSerieAndUsuarioSerie(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todas las series cuyos ID están en una lista específica (ids).
     * @param ids Lista de identificadores de las series.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByIdIn(List<Integer> ids, Pageable pageable);

    /**
     * Encuentra todas las series cuyos ID están en una lista específica (ids) y que pertenecen a un usuario específico (usuario).
     * @param ids Lista de identificadores de las series.
     * @param usuario El usuario al que pertenece la serie.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Serie que cumplen con los criterios de búsqueda.
     */

    Page<Serie> findAllByIdInAndUsuarioSerie(List<Integer> ids,Usuario usuario, Pageable pageable);

    /**
     * Encuentra una serie con un título específico (titulo) que pertenece a un usuario dado (usuario).
     * @param titulo El título dla serie.
     * @param usuario El usuario al que pertenece la serie.
     * @return Un objeto Serie específico que cumple con los criterios, null si no encuentra nada
     */


    Optional<Serie> findSerieByTituloAndUsuarioSerie(String titulo, Usuario usuario);
}
