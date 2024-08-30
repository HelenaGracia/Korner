package com.example.korner.repositorios;

import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.PlataformaVideojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Esta interfaz es un repositorio  utilizado para realizar operaciones de acceso a datos sobre la entidad PlataformaVideojuego.
 * Hereda de JpaRepository lo que significa que hereda automáticamente métodos básicos para CRUD, paginación y ordenación
 * sin necesidad de escribir ninguna implementación.
 */
@Repository
public interface PlataformaVideojuegoRepository extends JpaRepository<PlataformaVideojuego, Integer> {
}
