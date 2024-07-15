package com.example.korner.servicio;


import com.example.korner.modelo.Animes;
import com.example.korner.repositorios.AnimeRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimeServiceImpl extends AbstractService<Animes,Integer, AnimeRepository>{

    public AnimeServiceImpl(AnimeRepository animeRepository) {
        super(animeRepository);
    }
}
