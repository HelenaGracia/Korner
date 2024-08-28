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

/**
 * Es una clase de servicio que maneja las operaciones relacionadas con la entidad Usuario.
 * Hereda operaciones CRUD y de paginación genéricas de AbstractService.
 * Define métodos específicos para operaciones de consulta utilizando los métodos definidos en el repositorio.
 */

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

    /**
     * Este método es parte de la interfaz UserDetailsService de Spring Security, que se utiliza para cargar detalles
     * del usuario durante el proceso de autenticación. Esta implementación personalizada se encarga de buscar un
     * usuario en la base de datos basándose en el nombre de usuario proporcionado y de preparar la información
     * necesaria para el proceso de autenticación.Si el usuario está activo y se encuentra en la base de datos, se
     * guarda información del usuario en la sesión y se retorna el usuario encontrado, que debe implementar UserDetails,
     * esto permite que Spring Security utilice este objeto durante el proceso de autenticación y autorización.
     * @param username Cadena de texto que representa el nombre del usuario que se está intentado autenticar
     * @throws UsernameNotFoundException Excepción que se lanza si no se encuentra el usuario.
     */
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

    public Optional<Usuario> getByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
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
