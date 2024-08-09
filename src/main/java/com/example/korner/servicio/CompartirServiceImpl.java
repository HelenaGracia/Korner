package com.example.korner.servicio;


import com.example.korner.modelo.ElementoCompartido;
import com.example.korner.repositorios.CompartirRepository;
import org.springframework.stereotype.Service;

@Service
public class CompartirServiceImpl extends AbstractService<ElementoCompartido,Integer, CompartirRepository>{


    public CompartirServiceImpl(CompartirRepository compartirRepository) {
        super(compartirRepository);
    }
}
