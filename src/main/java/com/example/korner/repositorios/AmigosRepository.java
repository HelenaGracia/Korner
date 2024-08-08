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

    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteFalse(Usuario usuario, Pageable pageble);
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoFalseAndPendienteTrue(Usuario usuario, Pageable pageble);
    Page<Amigo> findAllByUsuarioOrigenAndBloqueadoTrue(Usuario usuario, Pageable pageble);
    Amigo findAmigoByUsuarioDestinoAndUsuarioOrigen(Usuario usuarioDestino, Usuario usuarioOrigen);
    List<Amigo> findAllByUsuarioOrigen(Usuario usuarioOrigen);

}
