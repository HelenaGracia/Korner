package com.example.korner.servicio;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.repositorios.GeneroElementoRepository;
import org.springframework.stereotype.Service;
/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad GeneroElementoCompartido.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 */
@Service
public class GeneroElementoServiceImpl extends AbstractService<GeneroElementoCompartido, Integer, GeneroElementoRepository>{
    public GeneroElementoServiceImpl(GeneroElementoRepository generoElementoRepository) {
        super(generoElementoRepository);
    }
}
