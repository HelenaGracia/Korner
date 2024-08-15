package com.example.korner.servicio;


import com.example.korner.modelo.ElementoCompartido;
import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.CompartirRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompartirServiceImpl extends AbstractService<ElementoCompartido,Integer, CompartirRepository>{

    private final CompartirRepository compartirRepository;

    public CompartirServiceImpl(CompartirRepository compartirRepository, CompartirRepository compartirRepository1) {
        super(compartirRepository);
        this.compartirRepository = compartirRepository1;
    }

    public List<ElementoCompartido> getAllAmigosByAmigoOrigen(Usuario amigos_usuarioOrigen){
        return compartirRepository.findAllByAmigos_UsuarioOrigen(amigos_usuarioOrigen);
    }

}
