package com.example.korner.servicio;

import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.UsuarioRepository;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UsuarioSecurityService extends AbstractService<Usuario,Integer, UsuarioRepository> implements UserDetailsService  {


    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;

    public UsuarioSecurityService(UsuarioRepository usuarioRepository, MessageSource messageSource) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> user = usuarioRepository.findBynombre(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            String errorMessage = messageSource.getMessage("user.not.found",null, Locale.getDefault());
            throw new UsernameNotFoundException(errorMessage);
        }

    }


}
