package com.example.korner.controladores;

import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.EmailService;
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
    private final EmailService emailService;

    public ContraseniaOlvidadaController(UsuarioSecurityService usuarioService, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    /**
     * Método el cual  muestra la vista asociada al archivo html contraseniaOlvidadaAutenticado
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/forgottenPasswordAutenticado")
    public String showPaginaAutenticado(){
        return "contraseniaOlvidadaAutenticado";
    }
    /**
     * Método el cual  muestra la vista asociada al archivo html contraseniaOlvidada
     * @return String del nombre de la vista que debe ser renderizada
     */
    @GetMapping("/forgottenPasswordShow")
    public String showPagina(){
        return "contraseniaOlvidada";
    }

    /**
     * Método el cual gestiona el proceso de recuperación de contraseña cuando un usuario ha olvidado su contraseña.
     * Se busca a un usuario a través de su correo electrónico, se genera una contraseña aleatoria para
     * este y se crea un correo electrónico el cual se envia a la dirección de correo electrónico del usuario proporcionándole
     * la nueva contraseña. A este método se accede cuando el usuario no ha iniciado sesión en la aplicación
     * @param correo String correo electrónico proporcionado por el usuario en el formulario de recuperación de contraseña
     * @param attributes RedirectAttributes permite añadir atributos que se envían como parte de una redirección, en este caso los mensaje de exíto o error
     * @return redirección a la endpoint /forgottenPasswordShow con un mensaje de éxito o error.
     */
    @PostMapping("/forgottenPassword")
    public String forgottenPassword(String correo, RedirectAttributes attributes){
        Optional<Usuario> usuario = usuarioService.getByCorreo(correo);
        if (usuario.isPresent()){
            StringBuilder passwordRandom = generarPassword(usuario);
            String mensaje = "Estimado/a " + usuario.get().getNombre() + " le proporcionamos la siguiente contraseña " +
                    "para que pueda acceder a su cuenta " + passwordRandom + " le recomendamos que la cambie por una" +
                    " personal en cuanto haya accedido en Ajustes -> cambiar contraseña. Muchas gracias por haces uso de" +
                    " nuestros servicios";
            emailService.sendEmail(usuario.get().getCorreo(),"Nueva contraseña", mensaje);
            attributes.addFlashAttribute("success", "Se le ha enviado un correo a " + usuario.get().getCorreo() +
                    " con la nueva contraseña, no se olvide de pulsar en click here");

        }else {
            attributes.addFlashAttribute("failed", "La dirección de correo no es la correcta o " +
                    "su cuenta puede estar elminada o desactivada, para más información póngase en contacto con nosotros " +
                    "en kornergestion@gmail.com");
        }
        return "redirect:/forgottenPasswordShow";
    }


    /**
     * Método el cual gestiona el proceso de recuperación de contraseña cuando un usuario ha olvidado su contraseña.
     * Se genera una contraseña aleatoria para
     * este y se crea un correo electrónico el cual se envia a la dirección de correo electrónico del usuario proporcionándole
     * la nueva contraseña. A este método se accede cuando el usuario ha iniciado sesión en la aplicación
     * @param correo String correo electrónico proporcionado por el usuario en el formulario de recuperación de contraseña
     * @param attributes RedirectAttributes permite añadir atributos que se envían como parte de una redirección, en este caso los mensaje de exíto o error
     * @return redirección al endpoint /forgottenPasswordAutenticado
     */
    @PostMapping("/forgottenPasswordAutenticado")
    public String forgottenPasswordAutenticado(String correo, RedirectAttributes attributes){
        Optional<Usuario> usuario = usuarioService.getByCorreo(correo);
        if (usuario.isPresent()){
            StringBuilder passwordRandom = generarPassword(usuario);
            String mensaje = "Estimado/a " + usuario.get().getNombre() + " le proporcionamos la siguiente contraseña " +
                    "para que pueda acceder a su cuenta " + passwordRandom + " le recomendamos que la cambie por una" +
                    " personal en cuanto haya accedido en Ajustes -> cambiar contraseña. Muchas gracias por haces uso de" +
                    " nuestros servicios";
            emailService.sendEmail(usuario.get().getCorreo(),"Nueva contraseña", mensaje);
            attributes.addFlashAttribute("success", "Se le ha enviado un correo a " + usuario.get().getCorreo() +
                    " con la nueva contraseña, no se olvide de pulsar en click here");

        }else {
            attributes.addFlashAttribute("failed", "La dirección de correo no es la correcta o " +
                    "su cuenta puede estar elminada o desactivada, para más información póngase en contacto con nosotros " +
                    "en kornergestion@gmail.com");
        }
        return "redirect:/forgottenPasswordAutenticado";
    }

    /**
     *Método para generar una contraseña alfanumérica aleatoria de una longitud específica
     * @param usuario Objeto de la clase Usuario
     * @return String de la contraseña generada
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
