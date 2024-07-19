package com.example.korner.servicio;

import com.example.korner.modelo.Libro;
import com.example.korner.repositorios.LibroRepository;
import org.springframework.stereotype.Service;

@Service
public class LibroServiceImpl extends AbstractService<Libro,Integer, LibroRepository>{

    public LibroServiceImpl(LibroRepository libroRepository) {
        super(libroRepository);
    }
}
