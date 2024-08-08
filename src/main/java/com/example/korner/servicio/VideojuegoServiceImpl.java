package com.example.korner.servicio;

import com.example.korner.modelo.*;
import com.example.korner.repositorios.VideojuegoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VideojuegoServiceImpl extends AbstractService<Videojuego,Integer, VideojuegoRepository>{
    private  final VideojuegoRepository videojuegoRepository;

    public VideojuegoServiceImpl(VideojuegoRepository videojuegoRepository, VideojuegoRepository videojuegoRepository1) {
        super(videojuegoRepository);

        this.videojuegoRepository = videojuegoRepository1;
    }

    public Page<Videojuego> getAllVideojuegos(Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByUsuarioVideojuego(usuario,pageable);
    }

    public Page<Videojuego> getAllVideojuegosByTitulo(String titulo, Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByTituloContainingIgnoreCaseAndUsuarioVideojuego(titulo, usuario, pageable);
    }

    public Page<Videojuego> getAllVideojuegosByPuntuacion(Integer puntuacion, Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByPuntuacionAndUsuarioVideojuego(puntuacion, usuario, pageable);
    }

    public  Page<Videojuego> getAllVideojuegosByGenero(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByGenerosVideojuegosAndUsuarioVideojuego(genero,usuario,pageable);
    }


    public  Page<Videojuego> getAllVideojuegosByYear(Integer year, Usuario usuario, Pageable pageable){
        return  videojuegoRepository.findAllByYearAndUsuarioVideojuego(year, usuario, pageable);
    }

    public  Page<Videojuego> getAllVideojuegosByPlataforma(PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByPlataformasVideojuegoAndUsuarioVideojuego(plataforma, usuario, pageable);
    }


    public Page<Videojuego> getAllVideojuegosByAllFiltros(
            Integer puntuacion, GeneroElementoCompartido genero, Integer year,
            PlataformaVideojuego plataforma, Usuario usuario, Pageable pageable){
        return videojuegoRepository.findAllByPuntuacionAndGenerosVideojuegosAndYearAndPlataformasVideojuegoAndUsuarioVideojuego(
                puntuacion,genero,year,plataforma, usuario, pageable);
    }
}
