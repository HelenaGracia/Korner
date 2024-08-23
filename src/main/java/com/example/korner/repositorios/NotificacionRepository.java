package com.example.korner.repositorios;

import com.example.korner.modelo.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    Page<Notificacion> findAllByUserToAndEstadoAndEstadoUsuario(String nombreUsuario, String estado, String estadoUsuario, Pageable pageable);
    List<Notificacion> findAllByUserToAndEstado(String nombreUsuario, String estado);
    List<Notificacion> findAllByUserToAndEstadoAndEstadoUsuario(String nombreUsuario, String estado, String estadoUsuario);
}
