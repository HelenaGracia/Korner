package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    Page<Libro> findAllByTituloContainingIgnoreCaseAndUsuarioLibro(String titulo, Usuario usuario, Pageable pageable);

    Page<Libro>findAllByUsuarioLibro(Usuario user, Pageable pageable);

    Page<Libro> findAllByPuntuacionAndUsuarioLibro(Integer puntuacion, Usuario usuario, Pageable pageable);

    Page<Libro> findAllByGenerosLibroAndUsuarioLibro(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    Page<Libro> findAllByYearAndUsuarioLibro(Integer year, Usuario usuario, Pageable pageable);

    Page<Libro> findAllByPlataformasLibroAndUsuarioLibro(Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Libro> findAllByPuntuacionAndGenerosLibroAndYearAndPlataformasLibroAndUsuarioLibro(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Libro> findAllByIdIn(List<Integer> ids, Pageable pageable);

    Page<Libro> findAllByIdInAndUsuarioLibro(List<Integer> ids,Usuario usuario, Pageable pageable);

}
