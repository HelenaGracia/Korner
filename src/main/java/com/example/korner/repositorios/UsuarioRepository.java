package com.example.korner.repositorios;


import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad Usuario.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Encuentra a un usuario por su nombre
     * @param username Cadena de texto que representa el nombre del usuario
     * @return Devuelve un Optional<Usuario>, que puede contener objeto un Usuario si se encuentra una coincidencia, o estar vacío si no se encuentra.
     */
    Optional<Usuario> findBynombre(String username);

    /**
     * Encuentra un usuario por su nombre pero solo si el usuario está activo.
     * @param username Cadena de texto que representa el nombre del usuario
     * @return Devuelve un Optional<Usuario>, que puede contener un objeto Usuario si se encuentra una coincidencia, o estar vacío si no se encuentra.
     */
    Optional<Usuario> findBynombreAndActivaTrue(String username);

    /**
     * Encuentra un usuario por su correo electrónico
     * @param correo Cadena de texto que representa el correo electrónico del usuario
     * @return Devuelve un Optional<Usuario>, que puede contener un objeto Usuario si se encuentra una coincidencia, o estar vacío si no se encuentra.
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Encuentra todos los usuarios por su nombre (ignorando mayusculas o minuscualas), que están activos y cuyo identificador no está en la lista proporcionada.
     * @param username Cadena de texto que representa el nombre del usuario
     * @param excludedId Lista de identificadores
     * @param pageble Información sobre la paginación y el orden de los resultados.
     * @return  Devuelve una página de objetos Usuario
     */
    Page<Usuario> findAllByNombreContainingIgnoreCaseAndIdNotInAndActivaTrue(String username, List<Integer> excludedId, Pageable pageble);

    /**
     * Encuentra todos los usuarios por su nombre (ignorando mayusculas o minuscualas), que están activos y cuyo identificador está en la lista proporcionada.
     * @param username Cadena de texto que representa el nombre del usuario
     * @param includeId Lista de identificadores
     * @return Devuelve una lista de objetos Usuario que cumple con los criterios
     */
    List<Usuario> findAllByNombreContainingIgnoreCaseAndIdIn(String username, List<Integer> includeId);

    /**
     * Encuentra todos los usuarios cuyo identificador no es el proporcionado
     * @param id identificador del usuario
     * @param pageble
     * @return  Devuelve una página de objetos Usuario
     */
    Page<Usuario>findAllByIdNot(Integer id,Pageable pageble);

}
