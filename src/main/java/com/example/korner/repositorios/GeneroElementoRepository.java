package com.example.korner.repositorios;

import com.example.korner.modelo.GeneroElementoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad GeneroElementoCompartido.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 */
@Repository
public interface GeneroElementoRepository extends JpaRepository<GeneroElementoCompartido, Integer> {

}
