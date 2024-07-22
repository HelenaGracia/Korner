package com.example.korner.servicio;

import com.example.korner.modelo.Serie;
import com.example.korner.repositorios.SerieRepository;
import org.springframework.stereotype.Service;

@Service
public class SerieServiceImpl extends AbstractService<Serie,Integer, SerieRepository>{

    public SerieServiceImpl(SerieRepository serieRepository) {
        super(serieRepository);
    }
}
