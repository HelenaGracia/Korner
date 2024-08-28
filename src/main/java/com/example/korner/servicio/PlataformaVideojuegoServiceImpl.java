package com.example.korner.servicio;


import com.example.korner.modelo.PlataformaVideojuego;

import com.example.korner.repositorios.PlataformaVideojuegoRepository;
import org.springframework.stereotype.Service;

/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad PlataformaVideojuego.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 */
@Service
public class PlataformaVideojuegoServiceImpl extends AbstractService<PlataformaVideojuego,Integer, PlataformaVideojuegoRepository>{

    public PlataformaVideojuegoServiceImpl(PlataformaVideojuegoRepository plataformaVideojuegoRepository) {
        super(plataformaVideojuegoRepository);
    }
}
