package com.example.korner.servicio;

import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.AmigosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmigoServiceImpl extends AbstractService<Amigo,Integer, AmigosRepository>{

    private final AmigosRepository amigosRepository;

    public AmigoServiceImpl(AmigosRepository amigosRepository, AmigosRepository amigosRepository1) {
        super(amigosRepository);
        this.amigosRepository = amigosRepository1;
    }


    public Page<Amigo> getAllAmigos (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalse(usuario,pageable);
    }

    public Page<Amigo> getAllSolicitudesPendientes (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteTrue(usuario,pageable);
    }

    public Page<Amigo> getAllAmigosBloqueados (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoTrue(usuario,pageable);
    }

    public Amigo getAmigo (Usuario usuarioDestino, Usuario usuarioOrigen) {
        return amigosRepository.findAmigoByUsuarioDestinoAndUsuarioOrigen(usuarioDestino,usuarioOrigen);
    }

    public List<Amigo> getAllAmigosList (Usuario usuarioOrigen) {
        return amigosRepository.findAllByUsuarioOrigen(usuarioOrigen);
    }

}
