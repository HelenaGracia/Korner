package com.example.korner.servicio;

import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.AmigosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad Amigo.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 * Define métodos específicos para operaciones de consulta utilizando los métodos definidos en el repositorio.
 */
@Service
public class AmigoServiceImpl extends AbstractService<Amigo,Integer, AmigosRepository>{

    private final AmigosRepository amigosRepository;

    public AmigoServiceImpl(AmigosRepository amigosRepository, AmigosRepository amigosRepository1) {
        super(amigosRepository);
        this.amigosRepository = amigosRepository1;
    }


    public Page<Amigo> getAllAmigos (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(usuario,pageable);
    }

    public Page<Amigo> getAllSolicitudesPendientes (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioDestinoAndBloqueadoFalseAndPendienteTrueAndUsuarioOrigen_ActivaTrue(usuario,pageable);
    }

    public Page<Amigo> getAllSolicitudesEnviadas (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteTrueAndUsuarioDestino_ActivaTrue(usuario,pageable);
    }

    public Page<Amigo> getAllAmigosBloqueados (Usuario usuario, Pageable pageable) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoTrueAndUsuarioDestino_ActivaTrue(usuario,pageable);
    }

    public Amigo getAmigo (Usuario usuarioDestino, Usuario usuarioOrigen) {
        return amigosRepository.findAmigoByUsuarioDestinoAndUsuarioOrigenAndUsuarioDestino_ActivaTrue(usuarioDestino,usuarioOrigen);
    }

    public List<Amigo> getAllAmigosList (Usuario usuarioOrigen) {
        return amigosRepository.findAllByUsuarioOrigen(usuarioOrigen);
    }
    public List<Amigo> getAllAmigosListPendientes (Usuario usuarioDestino) {
        return amigosRepository.findAllByUsuarioDestinoAndPendienteTrue(usuarioDestino);
    }

    public List<Amigo> getAllAmigosListNoBloqueadosNoPendientes (Usuario usuarioOrigen) {
        return amigosRepository.findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(usuarioOrigen);
    }

    public  Page<Amigo>getAllAmigosEnListaUsuarioDestino(Usuario usuarioOrigen,List<Usuario> listaUsuarioDestino, Pageable pageable){
        return amigosRepository.findAllByUsuarioOrigenAndUsuarioDestinoIn(usuarioOrigen, listaUsuarioDestino, pageable);
    }

}
