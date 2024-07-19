package com.example.korner.servicio;


import com.example.korner.modelo.Rol;
import com.example.korner.repositorios.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl extends AbstractService<Rol,Integer, RolRepository>{

    public RolServiceImpl(RolRepository rolRepository) {
        super(rolRepository);
    }
}
