package com.example.korner.repositorios;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmigosRepository extends JpaRepository<Amigo, Integer> {

    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(Usuario usuario, Pageable pageble);
    //Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalse(Usuario usuario, Pageable pageble);

    Page<Amigo> findAllByUsuarioDestinoAndBloqueadoFalseAndPendienteTrueAndUsuarioOrigen_ActivaTrue(Usuario usuario,Pageable pageble);
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteTrueAndUsuarioDestino_ActivaTrue(Usuario usuario,Pageable pageble);
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoTrueAndUsuarioDestino_ActivaTrue(Usuario usuario, Pageable pageble);
    Amigo findAmigoByUsuarioDestinoAndUsuarioOrigenAndUsuarioDestino_ActivaTrue(Usuario usuarioDestino, Usuario usuarioOrigen);
    List<Amigo> findAllByUsuarioOrigen(Usuario usuarioOrigen);
    List<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalseAndUsuarioDestino_ActivaTrue(Usuario usuarioOrigen);

    Page<Amigo> findAllByUsuarioOrigenAndUsuarioDestinoIn(Usuario usuarioOrigen,List<Usuario> listaUsuarioDestino, Pageable pageable);

}
