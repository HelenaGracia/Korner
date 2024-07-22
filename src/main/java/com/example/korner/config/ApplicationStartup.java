package com.example.korner.config;

import com.example.korner.modelo.Rol;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.RolServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ApplicationStartup implements ApplicationListener <ApplicationReadyEvent> {

    private final RolServiceImpl rolService;

    public ApplicationStartup(RolServiceImpl rolService) {
        this.rolService = rolService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
//        Set<Usuario> usuarios = new HashSet<>();
//        String[] rolesNames = {"SUPERADMIN", "ADMIN", "USER"};
//        int id = 1;
//
//        for (String roleName : rolesNames) {
//            Rol roles = new Rol(id, roleName, usuarios);
//            rolService.saveEntity(roles);
//            id++;
//        }
    }
}
