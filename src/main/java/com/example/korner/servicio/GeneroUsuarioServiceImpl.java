package com.example.korner.servicio;

import com.example.korner.modelo.Genero;
import com.example.korner.modelo.GeneroElementoCompartido;
import com.example.korner.repositorios.GeneroElementoRepository;
import com.example.korner.repositorios.GeneroUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class GeneroUsuarioServiceImpl extends AbstractService<Genero, Integer, GeneroUsuarioRepository>{
    public GeneroUsuarioServiceImpl(GeneroUsuarioRepository generoUsuarioRepository) {
        super(generoUsuarioRepository);
    }
}
