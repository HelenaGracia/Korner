package com.example.korner.repositorios;

import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {

    Page<Serie> findAllByTituloContainingIgnoreCaseAndUsuarioSerie(String titulo, Usuario usuario, Pageable pageable);

    Page<Serie>findAllByUsuarioSerie(Usuario user, Pageable pageable);

    Page<Serie> findAllByPuntuacionAndUsuarioSerie(Integer puntuacion, Usuario usuario, Pageable pageable);

    Page<Serie> findAllByGenerosSerieAndUsuarioSerie(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    Page<Serie> findAllByYearAndUsuarioSerie(Integer year, Usuario usuario, Pageable pageable);

    Page<Serie> findAllByPlataformasSerieAndUsuarioSerie(Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Serie> findAllByPuntuacionAndGenerosSerieAndYearAndPlataformasSerieAndUsuarioSerie(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Serie> findAllByIdIn(List<Integer> ids, Pageable pageable);

    Page<Serie> findAllByIdInAndUsuarioSerie(List<Integer> ids,Usuario usuario, Pageable pageable);

    Optional<Serie> findSerieByTituloAndUsuarioSerie(String titulo, Usuario usuario);
}
