package com.example.korner.repositorios;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    Page<Pelicula> findAllByTituloContainingIgnoreCaseAndUsuarioPelicula(String titulo, Usuario usuario, Pageable pageable);

    Page<Pelicula>findAllByUsuarioPelicula(Usuario user, Pageable pageable);

    Page<Pelicula> findAllByPuntuacionAndUsuarioPelicula(Integer puntuacion, Usuario usuario, Pageable pageable);



}
