package com.example.korner.servicio;

import com.example.korner.modelo.Usuario;
import com.example.korner.repositorios.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UsuarioSecurityService extends AbstractService<Usuario,Integer, UsuarioRepository> implements UserDetailsService  {

    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;
    private final HttpSession session;

    public UsuarioSecurityService(UsuarioRepository usuarioRepository, MessageSource messageSource, HttpSession session) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> user = usuarioRepository.findBynombre(username);
        if (user.isPresent()) {
            if(!user.get().getActiva()){
                String errorMessage = messageSource.getMessage("user.not.found",null, Locale.getDefault());
                throw new UsernameNotFoundException(errorMessage);
            }
            session.setAttribute("idusuario", user.get().getId());
            session.setAttribute("rutaImagen", user.get().getRutaImagen());
            session.setAttribute("userName",user.get().getNombre());
            return user.get();
        } else {
            String errorMessage = messageSource.getMessage("user.not.found",null, Locale.getDefault());
            throw new UsernameNotFoundException(errorMessage);
        }

    }

    public Optional<Usuario> getByName(String nombre) {
        return usuarioRepository.findBynombre(nombre);
    }

    public Optional<Usuario> getByNameUsuarioActivo(String nombre) {
        return usuarioRepository.findBynombreAndActivaTrue(nombre);
    }

    public Optional<Usuario> getByCorreo(String nombre) {
        return usuarioRepository.findByCorreo(nombre);
    }

    public Page<Usuario> getAllUsuarios(String username,Pageable pageable) {
        return usuarioRepository.findAllByNombreContainingIgnoreCase(username, pageable);
    }

    public Page<Usuario> getAllUsuariosSinListIdSinInactivos(String username, List<Integer> excludedId, Pageable pageble) {
        return usuarioRepository.findAllByNombreContainingIgnoreCaseAndIdNotInAndActivaTrue(username, excludedId,pageble);
    }

    public List<Usuario> getAllUsuariosEnListId(String username, List<Integer> includeId){
        return usuarioRepository.findAllByNombreContainingIgnoreCaseAndIdIn(username, includeId);
    }

    public Page<Usuario>getAllUsuariosMenosEste(Integer id,Pageable pageble){
        return usuarioRepository.findAllByIdNot(id,pageble);
    }

}
