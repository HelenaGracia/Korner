package com.example.korner.servicio;

import com.example.korner.modelo.Pelicula;
import com.example.korner.repositorios.PeliculaRepository;
import org.springframework.stereotype.Service;

@Service
public class PeliculaServiceImpl extends AbstractService<Pelicula,Integer, PeliculaRepository>{

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository) {
        super(peliculaRepository);
    }
}
