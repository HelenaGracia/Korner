package com.example.korner.servicio;


import com.example.korner.modelo.Rol;
import com.example.korner.repositorios.RolRepository;
import org.springframework.stereotype.Service;

/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad Rol.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 */
@Service
public class RolServiceImpl extends AbstractService<Rol,Integer, RolRepository>{

    public RolServiceImpl(RolRepository rolRepository) {
        super(rolRepository);
    }
}
