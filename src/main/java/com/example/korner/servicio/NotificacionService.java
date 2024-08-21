package com.example.korner.servicio;

import com.example.korner.modelo.Notificacion;
import com.example.korner.repositorios.NotificacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService extends AbstractService<Notificacion, Integer, NotificacionRepository> {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, NotificacionRepository notificacionRepository1) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository1;
    }

    public Page<Notificacion> getAllNotificacionesByUserAndEstado(String nombreUsuario, String estado, Pageable pageable){
        return notificacionRepository.findAllByUserToAndEstado(nombreUsuario,estado, pageable);
    }

    public List<Notificacion> getAllNotificacionesByUserAndEstadoList(String nombreUsuario, String estado){
        return notificacionRepository.findAllByUserToAndEstado(nombreUsuario,estado);
    }
    public List<Notificacion> getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(String nombreUsuario, String estado, String estadoUsuario){
        return notificacionRepository.findAllByUserToAndEstadoAndEstadoUsuario(nombreUsuario,estado, estadoUsuario);
    }
}
