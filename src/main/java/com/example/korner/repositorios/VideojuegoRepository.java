package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Videojuego.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository

public interface VideojuegoRepository extends JpaRepository<Videojuego, Integer> {

    /**
     * Encuentra todos los videojuegos cuyo título contenga el texto especificado (titulo), ignorando mayúsculas y minúsculas,
     * y que pertenezcan al usuario dado.
     * @param titulo Texto que debe estar contenido en el título del videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */
    Page<Videojuego> findAllByTituloContainingIgnoreCaseAndUsuarioVideojuego(String titulo, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos que pertenecen a un usuario específico (user).
     * @param user El usuario al que pertenecen los videojuegos.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego>findAllByUsuarioVideojuego(Usuario user, Pageable pageable);

    /**
     * Encuentra todos los videojuegos con una puntuación específica (puntuacion) que pertenecen a un usuario dado (usuario).
     * @param puntuacion La puntuación del videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByPuntuacionAndUsuarioVideojuego(Integer puntuacion, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos que pertenecen a un género específico (genero) y al usuario dado (usuario).
     * @param genero El género del videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByGenerosVideojuegosAndUsuarioVideojuego(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos por año de visualización específico (year) que pertenecen a un usuario dado (usuario).
     * @param year El año de visualización del videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByYearAndUsuarioVideojuego(Integer year, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos por la plataforma en la que lo han jugado (plataforma) y pertenecen a un usuario dado (usuario).
     * @param plataforma La plataforma en la que han jugado el videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByPlataformasVideojuegoAndUsuarioVideojuego(PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos que cumplen con una puntuación específica (puntuacion), pertenecen a un género específico (genero),
     * han sido vistos en el año especificado (year), han sido jugados en una plataforma específica (plataforma), y pertenecen a un usuario dado (usuario).
     * @param puntuacion  La puntuación del videojuego.
     * @param genero El género del videojuego.
     * @param year El año de visualización del videojuego
     * @param plataforma La plataforma en la que han jugado el videojuego
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByPuntuacionAndGenerosVideojuegosAndYearAndPlataformasVideojuegoAndUsuarioVideojuego(Integer puntuacion, GeneroElementoCompartido genero, Integer year, PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable);

    /**
     * Encuentra todos los videojuegos cuyos ID están en una lista específica (ids).
     * @param ids Lista de identificadores de los videojuegos.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByIdIn(List<Integer> ids, Pageable pageable);


    /**
     * Encuentra todos los videojuegos cuyos ID están en una lista específica (ids) y que pertenecen a un usuario específico (usuario).
     * @param ids Lista de identificadores de los videojuegos.
     * @param usuario El usuario al que pertenece el videojuego.
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Videojuego que cumplen con los criterios de búsqueda.
     */

    Page<Videojuego> findAllByIdInAndUsuarioVideojuego(List<Integer> ids,Usuario usuario, Pageable pageable);

    /**
     * Encuentra un videojuego con un título específico (titulo) que pertenece a un usuario dado (usuario).
     * @param titulo El título del videojuego.
     * @param usuario El usuario al que pertenece el videojuego.
     * @return Un objeto Videojuego específico que cumple con los criterios, null si no encuentra nada
     */

    Optional<Videojuego> findVideojuegoByTituloAndUsuarioVideojuego(String titulo, Usuario usuario);

}
