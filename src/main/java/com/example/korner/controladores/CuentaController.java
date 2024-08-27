package com.example.korner.controladores;


import com.example.korner.modelo.Genero;
import com.example.korner.modelo.Rol;
import com.example.korner.modelo.Usuario;
import com.example.korner.modeloValidaciones.UsuarioNuevo;
import com.example.korner.servicio.GeneroUsuarioServiceImpl;
import com.example.korner.servicio.RolServiceImpl;
import com.example.korner.servicio.UsuarioSecurityService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/creacion")
public class CuentaController {

    private final UsuarioSecurityService usuarioService;
    private final RolServiceImpl rolService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GeneroUsuarioServiceImpl generoUsuarioService;

    public CuentaController(UsuarioSecurityService usuarioService, RolServiceImpl rolService, BCryptPasswordEncoder bCryptPasswordEncoder, GeneroUsuarioServiceImpl generoUsuarioService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.generoUsuarioService = generoUsuarioService;
    }

    /**
     * showCuenta Metódo el cual obtiene una pagina en la cual hay un formulario para que un usuario pueda crear una cuenta personal
     *
     * @param model model se utiliza para pasar datos desde el controlador a la vista. En este caso estamos enviando la lista
     * de generos que existen, un listado de años desde 1900 hasta el año actual para que el usuario elija su año de nacimiento
     * y la creación de un nuevo objeto new Usuario para recoger los datos del formulario
     * @return se retorna al html cuenta donde se visualiza el formulario de creacion de usuario
     */

    @GetMapping
    public String showCuenta(Model model) {

        //Inicio List Desplegable Año Nacimiento
        List<Integer> options = new ArrayList<>();
        int anyo = LocalDate.now().getYear();
        for (int i = anyo; i >= 1900; i--) {
            options.add(i);
        }
        //Fin List Desplegable Año Nacimiento
        Usuario nuevoUsuario = new Usuario();
        List<Genero> listGenero = generoUsuarioService.getAll();
        model.addAttribute("generoUsuario", listGenero);
        model.addAttribute("options", options);
        model.addAttribute("nuevoUsuario", nuevoUsuario);
        return "cuenta" ;
    }

    /**
     * saveUsuario Método por el cual se recibe los datos proporcionados por el usuario, se revisan que los datos cumplan
     * las validaciones correspondientes y si no exite un usuario con ese nombreo o ese correo se procede a crear al usuario.
     * Se crea el usuario con los datos proporcionados,además se le proporciona un rol de user, una foto de perfil por defecto,
     * el inicio de sesion en la home por defecto y se activa la cuenta. La contraseña se guarda encriptada en BBDD.
     *
     * @param usuarioNuevo Recoge todos los datos proporcionados por el usuario en el formulario de creacion de cuenta
     * @param bindingResult Comprueba que no haya datos incorrectos de los datos proporcionados con las restricciones de la entidad.
     * Valida los datos de usuarioNuevo de la entidad UsuarioNuevo.
     * @param model Utilizamos el modelo para volver al html con mensajes explicativos de errores o exito, y para volver a enviar
     * los generos existentes y la lista de años de nacimiento.
     * @param attributes Utilizamos el attributes para redireccionar con mensajes explicativos de errores o exito.
     * @return al html cuenta o se redirecciona al endpoint /creacion o al endpoint /home si se ha creado con exito la cuenta
     */


    @PostMapping("/save")
    public String saveUsuario(@Validated @ModelAttribute(name = "nuevoUsuario") UsuarioNuevo usuarioNuevo,
                       BindingResult bindingResult, Model model, RedirectAttributes attributes){
        try {
            List<Integer> options = new ArrayList<>();
            int anyo = LocalDate.now().getYear();
            for (int i = anyo; i >= 1900; i--) {
                options.add(i);
            }
            List<Genero> listGenero = generoUsuarioService.getAll();
            model.addAttribute("generoUsuario", listGenero);
            model.addAttribute("options", options);

            if (bindingResult.hasErrors()){
                return "cuenta";
            } else if (usuarioService.getByName(usuarioNuevo.getNombre()).isPresent()) {
                if (usuarioService.getByCorreo(usuarioNuevo.getCorreo()).isPresent()) {
                    model.addAttribute("failedNombre", "Ya existe un usuario con ese nombre");
                    model.addAttribute("failedCorreo", "Ya existe un usuario con ese correo");
                    return "cuenta";
                }
                model.addAttribute("failedNombre", "Ya existe un usuario con ese nombre");
                return "cuenta";
            } else if (usuarioService.getByCorreo(usuarioNuevo.getCorreo()).isPresent()) {
                model.addAttribute("failedCorreo", "Ya existe un usuario con ese correo");
                return "cuenta";
            }
            Optional<Rol> role = rolService.getById(1);
            String password = bCryptPasswordEncoder.encode(usuarioNuevo.getContrasena());
            Usuario usuario = new Usuario();
            if (role.isPresent()) {
                usuario.setNombre(usuarioNuevo.getNombre());
                usuario.setCorreo(usuarioNuevo.getCorreo());
                usuario.setContrasena(password);
                usuario.setAnioNacimiento(usuarioNuevo.getAnioNacimiento());
                usuario.setGeneros(usuarioNuevo.getGeneros());
                usuario.setRole(role.get());
                usuario.setAjustesInicioSesion("home");
                usuario.setRutaImagen("/img/icon1.png");
                usuario.setActiva(true);
                usuarioService.saveEntity(usuario);
                return "redirect:/home";
            }

        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error al introducir los datos");
            }

        return "redirect:/creacion";
    }
}
