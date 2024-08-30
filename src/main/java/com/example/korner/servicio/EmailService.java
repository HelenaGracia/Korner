package com.example.korner.servicio;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Es una clase de servicio que se encarga de enviar correos electrónicos
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * Método que se encarga de enviar correos electrónicos
     * @param to Cadena de texto que representa la dirección de correo electronica del usuario que va a recibir el correo
     * @param subject Cadena de texto que representa el asunto del correo
     * @param body texto del que está compuesto el mensaje del correo
     */
    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kornergestion@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
