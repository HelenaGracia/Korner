package com.example.korner.servicio;

import com.example.korner.modelo.FormatoLibro;
import com.example.korner.modelo.PlataformaVideojuego;
import com.example.korner.repositorios.FormatoLibroRepository;
import com.example.korner.repositorios.PlataformaVideojuegoRepository;
import org.springframework.stereotype.Service;
/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad FormatoLibro.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 */
@Service
public class FormatoLibroServiceImpl extends AbstractService<FormatoLibro,Integer, FormatoLibroRepository>{


    public FormatoLibroServiceImpl(FormatoLibroRepository formatoLibroRepository) {
        super(formatoLibroRepository);
    }
}
