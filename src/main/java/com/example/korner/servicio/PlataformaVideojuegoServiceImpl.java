package com.example.korner.servicio;

import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.PlataformaVideojuego;
import com.example.korner.repositorios.PlataformaRepository;
import com.example.korner.repositorios.PlataformaVideojuegoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlataformaVideojuegoServiceImpl extends AbstractService<PlataformaVideojuego,Integer, PlataformaVideojuegoRepository>{

    public PlataformaVideojuegoServiceImpl(PlataformaVideojuegoRepository plataformaVideojuegoRepository) {
        super(plataformaVideojuegoRepository);
    }
}
