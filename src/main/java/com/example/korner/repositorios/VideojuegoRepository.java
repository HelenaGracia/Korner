package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VideojuegoRepository extends JpaRepository<Videojuego, Integer> {
    Page<Videojuego> findAllByTituloContainingIgnoreCaseAndUsuarioVideojuego(String titulo, Usuario usuario, Pageable pageable);

    Page<Videojuego>findAllByUsuarioVideojuego(Usuario user, Pageable pageable);

    Page<Videojuego> findAllByPuntuacionAndUsuarioVideojuego(Integer puntuacion, Usuario usuario, Pageable pageable);

    Page<Videojuego> findAllByGenerosVideojuegosAndUsuarioVideojuego(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    Page<Videojuego> findAllByYearAndUsuarioVideojuego(Integer year, Usuario usuario, Pageable pageable);

    Page<Videojuego> findAllByPlataformasVideojuegoAndUsuarioVideojuego(PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable);

    Page<Videojuego> findAllByPuntuacionAndGenerosVideojuegosAndYearAndPlataformasVideojuegoAndUsuarioVideojuego(Integer puntuacion, GeneroElementoCompartido genero, Integer year, PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable);

    Page<Videojuego> findAllByIdIn(List<Integer> ids, Pageable pageable);

    Page<Videojuego> findAllByIdInAndUsuarioVideojuego(List<Integer> ids,Usuario usuario, Pageable pageable);

}
