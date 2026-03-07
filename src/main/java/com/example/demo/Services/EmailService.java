package com.example.demo.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Environment env; // opcional para armar URLs

    public EmailService(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }
    // !importante¡/ /
    // --- EXISTENTE: reset password en texto plano ---
    public void sendPasswordResetEmail(String to, String token) {
        String appUrl = env.getProperty("app.frontend.url", "http://localhost:3000");
        String resetLink = appUrl + "/reset-password?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Restablecer contraseña - EduGame");
        msg.setText("Hola,\n\nRecibimos una solicitud de restablecimiento de contraseña. " +
                "Usa este enlace para cambiar tu contraseña (válido 15 min):\n\n" + resetLink +
                "\n\nSi no pediste esto, ignora este correo.");
        mailSender.send(msg);
    }
    // !importante¡/ /
    // --- EXISTENTE: correo simple ---
    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(contenido);
        mailSender.send(mensaje);
    }
    // !importante¡/ /
    // --- EXISTENTE: correo masivo en texto plano ---
    public void enviarCorreoMasivo(String[] destinatarios, String asunto, String contenido) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatarios);
        mensaje.setSubject(asunto);
        mensaje.setText(contenido);
        mailSender.send(mensaje);
    }

    // !importante¡/ /
    //POST http://localhost:8080/api/correos/masivo/texto//
    // --- EXISTENTE: correos individuales ---
    public void enviarCorreosIndividuales(List<String> destinatarios, String asunto, String contenido) {
        for (String destinatario : destinatarios) {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);
            mailSender.send(mensaje);
        }
    }
    // !importante¡/ /
    // --- NUEVO: correo HTML individual ---
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo HTML: " + e.getMessage());
        }
    }
    // !importante¡/ /
//    POST http://localhost:8080/api/correos/masivo/html / /

    // --- NUEVO: correo HTML masivo ---
    public void enviarCorreoHtmlMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        for (String destinatario : destinatarios) {
            enviarCorreoHtml(destinatario, asunto, contenidoHtml);
        }
    }

    // --- Ejemplo de plantilla HTML ---
    public String plantillaBienvenida() {
        return """
                   <html>
                       <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                           <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;">
                               <h1 style="color: #4CAF50; text-align: center;">¡Bienvenido a ShenmyKappai!</h1>
                
                               <p style="font-size: 16px; text-align: justify;">
                                   Te invitamos a este mundo educativo donde podrás fortalecer tus habilidades y convertirte en una persona más fuerte e inteligente.
                               </p>
                
                               <p style="font-size: 16px; text-align: justify;">
                                   Estamos emocionados de que te unas a nuestra comunidad. Prepárate para aprender, crecer y alcanzar tus metas.
                               </p>
                
                               <div style="text-align: center; margin-top: 20px;">
                                   <a href="https://www.edugame.com"\s
                                      style="text-decoration: none; color: white; background-color: #4CAF50; padding: 10px 20px; border-radius: 5px; font-size: 16px;">
                                       ¡Comienza ahora!
                                   </a>
                               </div>
                
                               <p style="font-size: 14px; text-align: center; margin-top: 20px; color: #777;">
                                   Si tienes alguna pregunta, no dudes en contactarnos en\s
                                   <a href="mailto:soporte@edugame.com" style="color: #4CAF50;">soporte@edugame.com</a>.
                               </p>
                           </div>
                       </body>
                       </html>
                """;
    }
}
