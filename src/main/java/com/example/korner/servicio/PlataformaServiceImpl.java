package com.example.korner.servicio;

import com.example.korner.modelo.Plataforma;
import com.example.korner.repositorios.PlataformaRepository;
import org.springframework.stereotype.Service;

/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad Plataforma.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 */
@Service
public class PlataformaServiceImpl extends AbstractService<Plataforma,Integer, PlataformaRepository>{
    public PlataformaServiceImpl(PlataformaRepository plataformaRepository) {
        super(plataformaRepository);
    }
}
