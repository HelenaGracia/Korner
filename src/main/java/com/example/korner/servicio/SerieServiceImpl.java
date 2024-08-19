package com.example.korner.servicio;

import com.example.korner.modelo.*;
import com.example.korner.repositorios.SerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieServiceImpl extends AbstractService<Serie,Integer, SerieRepository>{

    private final SerieRepository serieRepository;
    public SerieServiceImpl(SerieRepository serieRepository, SerieRepository serieRepository1) {
        super(serieRepository);
        this.serieRepository = serieRepository1;
    }

    public Page<Serie> getAllSeries(Usuario usuario, Pageable pageable){
        return serieRepository.findAllByUsuarioSerie(usuario, pageable);
    }

    public Page<Serie> getAllSeriesByTitulo(String titulo, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByTituloContainingIgnoreCaseAndUsuarioSerie(titulo, usuario, pageable);
    }

    public Page<Serie> getAllSeriesByPuntuacion(Integer puntuacion, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByPuntuacionAndUsuarioSerie(puntuacion, usuario, pageable);
    }

    public  Page<Serie> getAllSeriesByGenero(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByGenerosSerieAndUsuarioSerie(genero,usuario,pageable);
    }

    public  Page<Serie> getAllSeriesByYear(Integer year, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByYearAndUsuarioSerie(year, usuario, pageable);
    }

    public  Page<Serie> getAllSeriesByPlataforma(Plataforma plataforma, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByPlataformasSerieAndUsuarioSerie(plataforma, usuario, pageable);
    }

    public Page<Serie>getAllSeriesByAllFiltros(
            Integer puntuacion, GeneroElementoCompartido genero, Integer year,
            Plataforma plataforma, Usuario usuario, Pageable pageable){
        return serieRepository.findAllByPuntuacionAndGenerosSerieAndYearAndPlataformasSerieAndUsuarioSerie
                (puntuacion, genero, year, plataforma, usuario, pageable);
    }

    public Page<Serie> getAllSeriesCompartidosByListId(List<Integer> listId, Pageable page){
        return serieRepository.findAllByIdIn(listId, page);
    }

    public Page<Serie> getAllSeriesCompartidosByListIdAndUsuario(List<Integer> listId, Usuario usuario, Pageable page){
        return serieRepository.findAllByIdInAndUsuarioSerie(listId, usuario, page);
    }

    public Optional<Serie> getSerieByTituloAndUsuario(String titulo, Usuario usuario){
        return serieRepository.findSerieByTituloAndUsuarioSerie(titulo, usuario);
    }
}
