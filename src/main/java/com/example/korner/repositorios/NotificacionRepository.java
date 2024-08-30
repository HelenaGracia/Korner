package com.example.korner.repositorios;

import com.example.korner.modelo.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Notificación.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    /**
     * Encuentra los objetos Notificación que recibe un usuario especificado (nombreUsuario), que se encuentran en un
     * estado especificado (estado) y con un estado de cuenta especificado (estadoUsuario)
     * @param nombreUsuario Cadena de texto que referencia el nombre del usuario al que va dirigida la notificación
     * @param estado Cadena de texto que referecia el estado en el que se encuentra la notificación
     * @param estadoUsuario Cadena de texto que referencia el estado en el que se encuentra la cuenta del usuario del que procede la notificación
     * @param pageable Información sobre la paginación y el orden de los resultados.
     * @return Una página de objetos Notificación que cumplen con los criterios de búsqueda.
     */
    Page<Notificacion> findAllByUserToAndEstadoAndEstadoUsuario(String nombreUsuario, String estado, String estadoUsuario, Pageable pageable);

    /**
     * Encuentra los objetos Notificación que recibe un usuario especificado (nombreUsuario), que se encuentran en un
     * estado especificado (estado)
     * @param nombreUsuario Cadena de texto que referencia el nombre del usuario al que va dirigida la notificación
     * @param estado Cadena de texto que referecia el estado en el que se encuentra la notificación
     * @return Lista de objetos Notificación que cumplen con los criterios de búsqueda.
     */
    List<Notificacion> findAllByUserToAndEstado(String nombreUsuario, String estado);
    /**
     * Encuentra los objetos Notificación que recibe un usuario especificado (nombreUsuario), que se encuentran en un
     * estado especificado (estado) y con un estado de cuenta especificado (estadoUsuario)
     * @param nombreUsuario Cadena de texto que referencia el nombre del usuario al que va dirigida la notificación
     * @param estado Cadena de texto que referecia el estado en el que se encuentra la notificación
     * @param estadoUsuario Cadena de texto que referencia el estado en el que se encuentra la cuenta del usuario del que procede la notificación
     * @return Una lista de objetos Notificación que cumplen con los criterios de búsqueda.
     */
    List<Notificacion> findAllByUserToAndEstadoAndEstadoUsuario(String nombreUsuario, String estado, String estadoUsuario);
}
