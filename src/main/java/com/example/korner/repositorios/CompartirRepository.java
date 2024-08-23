package com.example.korner.repositorios;


import com.example.korner.modelo.ElementoCompartido;
import com.example.korner.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompartirRepository extends JpaRepository<ElementoCompartido, Integer> {

    List<ElementoCompartido> findAllByAmigos_UsuarioOrigenAndAmigos_UsuarioDestino_ActivaTrue(Usuario amigos_usuarioOrigen);

}
