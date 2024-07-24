package com.example.korner.servicio;

import com.example.korner.modelo.Plataforma;
import com.example.korner.repositorios.PlataformaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlataformaServiceImpl extends AbstractService<Plataforma,Integer, PlataformaRepository>{
    public PlataformaServiceImpl(PlataformaRepository plataformaRepository) {
        super(plataformaRepository);
    }
}
