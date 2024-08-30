package com.example.korner.repositorios;


import com.example.korner.modelo.ElementoCompartido;
import com.example.korner.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad ElementoCompartido.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 * Crea consultas SQL personalizadas mediante la creación de métodos construidos siguiendo las convenciones de nombres de Spring Data JPA,
 * Spring interpreta el nombre del método y genera la consulta SQL correspondiente
 */

@Repository
public interface CompartirRepository extends JpaRepository<ElementoCompartido, Integer> {
    /**
     *Encuentra todos los objetos ElementoCompartido donde el UsuarioOrigen es el usuario especificado y el UsuarioDestino está activo.
     * @param amigos_usuarioOrigen El usuario origen para el cual se busca la lista de objetos ElementoCompartido
     * @return Devuelve una lista de objetos ElementoCompartido que cumple con los criterios
     */
    List<ElementoCompartido> findAllByAmigos_UsuarioOrigenAndAmigos_UsuarioDestino_ActivaTrue(Usuario amigos_usuarioOrigen);

}
