package com.example.korner.repositorios;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Amigo.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository
public interface AmigosRepository extends JpaRepository<Amigo, Integer> {
    /**
     * Encuentra todos los objetos amigo del usuario especificado (como usuarioOrigen) que no están bloqueados, no están pendientes,
     * y donde el usuarioDestino está activo.
     * @param usuario  El usuario origen para el cual se busca la lista de objetos amigo
     * @param pageble  Información sobre la paginación y el orden de los resultados
     * @return Una página de objetos Amigo (paginada), según los criterios de búsqueda
     */
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(Usuario usuario, Pageable pageble);

    /**
     * Encuentra todos los objetos amigo donde el usuario es el usuarioDestino, la amistad no está bloqueada, está pendiente, y el usuarioOrigen está activo.
     * @param usuario El usuario destino para el cual se busca la lista de objetos amigo
     * @param pageble Información sobre la paginación y el orden de los resultados
     * @return Una página de objetos Amigo (paginada), según los criterios de búsqueda
     */
    Page<Amigo> findAllByUsuarioDestinoAndBloqueadoFalseAndPendienteTrueAndUsuarioOrigen_ActivaTrue(Usuario usuario,Pageable pageble);

    /**
     * Encuentra todos los objetos amigo donde el usuario es el usuarioOrigen, la amistad no está bloqueada, está pendiente, y el usuarioDestino está activo.
     * @param usuario El usuario origen para el cual se busca la lista de objetos amigo
     * @param pageble Información sobre la paginación y el orden de los resultados
     * @return Una página de objetos Amigo (paginada), según los criterios de búsqueda
     */
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteTrueAndUsuarioDestino_ActivaTrue(Usuario usuario,Pageable pageble);

    /**
     * Encuentra todos los objetos amigo donde el usuario es el usuarioOrigen, la amistad está bloqueada y el usuarioDestino está activo.
     * @param usuario El usuario origen para el cual se busca la lista de objetos amigo
     * @param pageble Información sobre la paginación y el orden de los resultados
     * @return Una página de objetos Amigo (paginada), según los criterios de búsqueda
     */
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoTrueAndUsuarioDestino_ActivaTrue(Usuario usuario, Pageable pageble);

    /**
     * Encuentra un objeto amigo específico basado en un usuarioDestino, usuarioOrigen, y donde el usuarioDestino está activo.
     * @param usuarioDestino un usuario de la aplicación
     * @param usuarioOrigen un usuario de la aplicación
     * @return Un único objeto Amigo que cumple con los criterios, o null si no se encuentra ninguna coincidencia.
     */
    Amigo findAmigoByUsuarioDestinoAndUsuarioOrigenAndUsuarioDestino_ActivaTrue(Usuario usuarioDestino, Usuario usuarioOrigen);

    /**
     *  Encuentra todos los objetos amigo para un usuarioOrigen específico.
     * @param usuarioOrigen El usuario origen para el cual se busca la lista de objetos amigo
     * @return  Una lista de objetos Amigo.
     */
    List<Amigo> findAllByUsuarioOrigen(Usuario usuarioOrigen);

    /**
     * Encuentra todos los objetos amigo que están en estado de pendiente  donde el usuarioDestino es el usuario especificado.
     * @param usuarioDestino El usuario destino para el cual se busca la lista de objetos amigo
     * @return  Una lista de objetos Amigo que cumplen con los criterios
     */
    List<Amigo> findAllByUsuarioDestinoAndPendienteTrue(Usuario usuarioDestino);

    /**
     * Encuentra todos los objetos amigo del usuario especificado (como usuarioOrigen) que no están bloqueados, no están pendientes,
     * donde el usuarioDestino está activo.
     * @param usuarioOrigen El usuario origen para el cual se busca la lista de objetos amigo
     * @return Una lista de objetos Amigo que cumple con los criterios
     */
    List<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(Usuario usuarioOrigen);

    /**
     *  Encuentra todos los objetos amigo donde el usuarioOrigen es el usuario especificado y el usuarioDestino está en una
     *  lista específica de usuarios (listaUsuarioDestino).
     * @param usuarioOrigen El usuario origen para el cual se busca la lista de objetos amigo
     * @param listaUsuarioDestino lista de usuarios destino
     * @param pageable
     * @return Una página de objetos Amigo que cumplen con los criterios.
     */
    Page<Amigo> findAllByUsuarioOrigenAndUsuarioDestinoIn(Usuario usuarioOrigen,List<Usuario> listaUsuarioDestino, Pageable pageable);

}
