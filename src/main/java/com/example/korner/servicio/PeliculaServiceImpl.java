package com.example.korner.servicio;

import com.example.korner.modelo.Pelicula;
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
}
