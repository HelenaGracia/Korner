package com.example.korner.repositorios;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    Page<Pelicula> findAllByTituloContainingIgnoreCaseAndUsuarioPelicula(String titulo, Usuario usuario, Pageable pageable);

    Page<Pelicula>findAllByUsuarioPelicula(Usuario user, Pageable pageable);

    Page<Pelicula> findAllByPuntuacionAndUsuarioPelicula(Integer puntuacion, Usuario usuario, Pageable pageable);

    Page<Pelicula> findAllByGenerosPeliculaAndAndUsuarioPelicula(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    Page<Pelicula> findAllByYearAndUsuarioPelicula(Integer year, Usuario usuario, Pageable pageable);

    Page<Pelicula> findAllByPlataformasPeliculaAndUsuarioPelicula(Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Pelicula> findAllByPuntuacionAndGenerosPeliculaAndYearAndPlataformasPeliculaAndUsuarioPelicula(
            Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);



}
