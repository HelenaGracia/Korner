package com.example.korner.repositorios;


import com.example.korner.modelo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    Page<Anime> findAllByTituloContainingIgnoreCaseAndUsuarioAnime(String titulo, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByUsuarioAnime(Usuario user, Pageable pageable);

    Page<Anime> findAllByPuntuacionAndUsuarioAnime(Integer puntuacion, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByGenerosAnimeAndUsuarioAnime(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByYearAndUsuarioAnime(Integer year, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByPlataformasAnimeAndUsuarioAnime(Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByPuntuacionAndGenerosAnimeAndYearAndPlataformasAnimeAndUsuarioAnime(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable);

    Page<Anime> findAllByIdIn(List<Integer> ids, Pageable pageable);

    Page<Anime> findAllByIdInAndUsuarioAnime(List<Integer> ids,Usuario usuario, Pageable pageable);


}
