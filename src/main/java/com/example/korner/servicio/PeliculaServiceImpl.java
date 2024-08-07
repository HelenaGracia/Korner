package com.example.korner.servicio;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.modelo.Pelicula;
import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.PeliculaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PeliculaServiceImpl extends AbstractService<Pelicula,Integer, PeliculaRepository>{
    private  final PeliculaRepository peliculaRepository;

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository, PeliculaRepository peliculaRepository1) {
        super(peliculaRepository);

        this.peliculaRepository = peliculaRepository1;
    }

    public Page<Pelicula> getAllPeliculas(Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByUsuarioPelicula(usuario,pageable);
    }

    public Page<Pelicula> getAllPeliculasByTitulo(String titulo, Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByTituloContainingIgnoreCaseAndUsuarioPelicula(titulo, usuario, pageable);
    }

    public Page<Pelicula> getAllPeliculasByPuntuacion(Integer puntuacion, Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByPuntuacionAndUsuarioPelicula(puntuacion, usuario, pageable);
    }

    public  Page<Pelicula> getAllPeliculasByGenero(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByGenerosPeliculaAndUsuarioPelicula(genero,usuario,pageable);
    }

    public  Page<Pelicula> getAllPeliculasByYear(Integer year, Usuario usuario, Pageable pageable){
        return  peliculaRepository.findAllByYearAndUsuarioPelicula(year, usuario, pageable);
    }

    public  Page<Pelicula> getAllPeliculasByPlataforma(Plataforma plataforma, Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByPlataformasPeliculaAndUsuarioPelicula(plataforma, usuario, pageable);
    }

    public Page<Pelicula>getAllPeliculasByAllFiltros(
            Integer puntuacion, GeneroElementoCompartido genero, Integer year,
            Plataforma plataforma, Usuario usuario, Pageable pageable){
        return peliculaRepository.findAllByPuntuacionAndGenerosPeliculaAndYearAndPlataformasPeliculaAndUsuarioPelicula(
                puntuacion,genero,year,plataforma, usuario, pageable);
    }
}
