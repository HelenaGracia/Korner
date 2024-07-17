package com.example.korner.servicio;

import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.repositorios.GeneroElementoRepository;
import org.springframework.stereotype.Service;

@Service
public class GeneroElementoServiceImpl extends AbstractService<GeneroElementoCompartido, Integer, GeneroElementoRepository>{
    public GeneroElementoServiceImpl(GeneroElementoRepository generoElementoRepository) {
        super(generoElementoRepository);
    }
}
