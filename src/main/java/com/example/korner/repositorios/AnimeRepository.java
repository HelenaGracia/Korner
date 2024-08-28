package com.example.korner.repositorios;


import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Anime.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    /**
     * Encuentra todos los animes cuyo título contenga el texto especificado (titulo), ignorando mayúsculas y minúsculas,
     * y que pertenezcan al usuario dado.
     * @param titulo Texto que debe estar contenido en el título del anime.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByTituloContainingIgnoreCaseAndUsuarioAnime(String titulo, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes que pertenecen a un usuario específico (user).
     * @param user El usuario al que pertenecen los animes.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByUsuarioAnime(Usuario user, Pageable pageable);

    /**
     * Encuentra todos los animes con una puntuación específica (puntuacion) que pertenecen a un usuario dado (usuario).
     * @param puntuacion La puntuación del anime.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByPuntuacionAndUsuarioAnime(Integer puntuacion, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes que pertenecen a un género específico (genero) y al usuario dado (usuario).
     * @param genero El género del anime.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByGenerosAnimeAndUsuarioAnime(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes por año de visualización específico (year) que pertenecen a un usuario dado (usuario).
     * @param year El año de visualización del anime.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByYearAndUsuarioAnime(Integer year, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes por la plataforma en la que lo han visto (plataforma) y pertenecen a un usuario dado (usuario).
     * @param plataforma La plataforma en la que han visto el anime.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByPlataformasAnimeAndUsuarioAnime(Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes que cumplen con una puntuación específica (puntuacion), pertenecen a un género específico (genero),
     * han sido vistos en el año especificado (year), han sido vistos en una plataforma específica (plataforma), y pertenecen a un usuario dado (usuario).
     * @param puntuacion  La puntuación del anime.
     * @param genero El género del anime.
     * @param year El año de visualización del anime
     * @param plataforma La plataforma de visualización del anime
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByPuntuacionAndGenerosAnimeAndYearAndPlataformasAnimeAndUsuarioAnime(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los animes cuyos Id están en una lista específica (ids).
     * @param ids Lista de identificadores de los animes.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByIdIn(List<Integer> ids, Pageable pageable);

    /**
     * Encuentra todos los animes cuyos ID están en una lista específica (ids) y que pertenecen a un usuario específico (usuario).
     * @param ids Lista de identificadores de los animes.
     * @param usuario El usuario al que pertenece el anime.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Anime que cumplen con los criterios de búsqueda.
     */
    Page<Anime> findAllByIdInAndUsuarioAnime(List<Integer> ids,Usuario usuario, Pageable pageable);

    /**
     * Encuentra un anime con un título específico (titulo) que pertenece a un usuario dado (usuario).
     * @param titulo El título del anime.
     * @param usuario El usuario al que pertenece el anime.
     * @return Un objeto Anime específico que cumple con los criterios, null si no encuentra nada
     */
    Optional<Anime> findAnimeByTituloAndUsuarioAnime(String titulo,Usuario usuario);

}
