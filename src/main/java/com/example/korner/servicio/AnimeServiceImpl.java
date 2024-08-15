package com.example.korner.servicio;


import com.example.korner.modelo.*;
import com.example.korner.repositorios.AnimeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimeServiceImpl extends AbstractService<Anime,Integer, AnimeRepository>{

    private final AnimeRepository animeRepository;

    public AnimeServiceImpl(AnimeRepository animeRepository, AnimeRepository animeRepository1) {
        super(animeRepository);
        this.animeRepository = animeRepository1;
    }

    public Page<Anime> getAllAnimes(Usuario usuario, Pageable pageable){
        return animeRepository.findAllByUsuarioAnime(usuario,pageable);
    }

    public Page<Anime> getAllAnimesByTitulo(String titulo, Usuario usuario, Pageable pageable){
        return animeRepository.findAllByTituloContainingIgnoreCaseAndUsuarioAnime(titulo, usuario, pageable);
    }

    public Page<Anime> getAllAnimesByPuntuacion(Integer puntuacion, Usuario usuario, Pageable pageable){
        return animeRepository.findAllByPuntuacionAndUsuarioAnime(puntuacion, usuario, pageable);
    }

    public  Page<Anime> getAllAnimesByGenero(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable){
        return animeRepository.findAllByGenerosAnimeAndUsuarioAnime(genero,usuario,pageable);
    }

    public  Page<Anime> getAllAnimesByYear(Integer year, Usuario usuario, Pageable pageable){
        return  animeRepository.findAllByYearAndUsuarioAnime(year, usuario, pageable);
    }

    public  Page<Anime> getAllAnimesByPlataforma(Plataforma plataforma, Usuario usuario, Pageable pageable){
        return animeRepository.findAllByPlataformasAnimeAndUsuarioAnime(plataforma, usuario, pageable);
    }

    public Page<Anime>getAllAnimesByAllFiltros(Integer puntuacion, GeneroElementoCompartido genero, Integer year, Plataforma plataforma, Usuario usuario, Pageable pageable){
        return animeRepository.findAllByPuntuacionAndGenerosAnimeAndYearAndPlataformasAnimeAndUsuarioAnime(
                puntuacion,genero,year,plataforma, usuario, pageable);
    }

    public Page<Anime> getAllAnimesCompartidosByListId (List<Integer> listId, Pageable page){
        return animeRepository.findAllByIdIn(listId, page);
    }

    public Page<Anime> getAllAnimesCompartidosByListIdAndUsuario (List<Integer> listId,Usuario usuario, Pageable page){
        return animeRepository.findAllByIdInAndUsuarioAnime(listId,usuario, page);
    }

    public Optional<Anime> getAnimeByTituloAndUsuarioAnime (String titulo,Usuario usuario){
        return animeRepository.findAnimeByTituloAndUsuarioAnime(titulo,usuario);
    }
}
