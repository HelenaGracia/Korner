package com.example.korner.servicio;

import com.example.korner.modelo.Amigo;
import com.example.korner.repositorios.AmigosRepository;
import org.springframework.stereotype.Service;

@Service
public class AmigoServiceImpl extends AbstractService<Amigo,Integer, AmigosRepository>{

    public AmigoServiceImpl(AmigosRepository amigosRepository) {
        super(amigosRepository);
    }
}
