package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.UsuarioSecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Optional;

@Controller
public class ContraseniaOlvidadaController {
    private final UsuarioSecurityService usuarioService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ContraseniaOlvidadaController(UsuarioSecurityService usuarioService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/forgottenPasswordAutenticado")
    public String showPaginaAutenticado(){
        return "contraseniaOlvidadaAutenticado";
    }
    @GetMapping("/forgottenPasswordShow")
    public String showPagina(){
        return "contraseniaOlvidada";
    }

    @PostMapping("/forgottenPassword")
    public String forgottenPassword(String correo, RedirectAttributes attributes){
        Optional<Usuario> usuario = usuarioService.getByCorreo(correo);
        if (usuario.isPresent()){
            StringBuilder passwordRandom = generarPassword(usuario);
            //Todo Enviar correo con la nueva contraseña
            attributes.addFlashAttribute("success", "Se le ha enviado un correo a la " +
                    "dirección proporcionada con la nueva contraseña, no se olvide de pulsar en click here " + passwordRandom);

        }else {
            attributes.addFlashAttribute("failed", "La dirección de correo no es la correcta o " +
                    "su cuenta puede estar elminada o desactivada, para más información póngase en contacto con nosotros " +
                    "en korneradministracion@gmail.com");
        }
        return "redirect:/forgottenPasswordShow";
    }



    @PostMapping("/forgottenPasswordAutenticado")
    public String forgottenPasswordAutenticado(String correo, RedirectAttributes attributes){
        Optional<Usuario> usuario = usuarioService.getByCorreo(correo);
        if (usuario.isPresent()){
            // Método para generar una contraseña alfanumérica aleatoria de una longitud específica
            // Rango ASCII – alfanumérico (0-9, a-z, A-Z)
            StringBuilder passwordRandom = generarPassword(usuario);
            //Todo Enviar correo con la nueva contraseña
            attributes.addFlashAttribute("success", "Se le ha enviado un correo a la " +
                    "dirección proporcionada con la nueva contraseña, no se olvide de pulsar en click here " + passwordRandom);

        }else {
            attributes.addFlashAttribute("failed", "La dirección de correo no es la correcta o " +
                    "su cuenta puede estar elminada o desactivada, para más información póngase en contacto con nosotros " +
                    "en korneradministracion@gmail.com");
        }
        return "redirect:/forgottenPasswordAutenticado";
    }

    /** Método para generar una contraseña alfanumérica aleatoria de una longitud específica
     *
     * @param usuario
     * @return string con la contraseña generada
     */
    private StringBuilder generarPassword(Optional<Usuario> usuario) {

        // Rango ASCII – alfanumérico (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        // cada iteración del bucle elige aleatoriamente un carácter del dado
        // rango ASCII y lo agrega a la instancia `StringBuilder`
        for (int i = 0; i < 10; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        usuario.get().setContrasena((bCryptPasswordEncoder.encode(sb)));
        usuarioService.saveEntity(usuario.get());
        return sb;
    }
}
