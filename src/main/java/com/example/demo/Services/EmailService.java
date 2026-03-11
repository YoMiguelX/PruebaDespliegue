package com.example.demo.Services;
import org.springframework.beans.factory.annotation.Value;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    private final String remitente = "no-reply@tudominio.com"; // remitente verificado en SendGrid

    // Reset password
    public void sendPasswordResetEmail(String to, String token) throws IOException {
        String resetLink = "https://tu-frontend.com/reset-password?token=" + token;
        String contenido = "Hola,\n\nRecibimos una solicitud de restablecimiento de contraseña. " +
                "Usa este enlace para cambiar tu contraseña (válido 15 min):\n\n" + resetLink +
                "\n\nSi no pediste esto, ignora este correo.";
        enviarCorreo(to, "Restablecer contraseña - EduGame", contenido);
    }

    // Correo simple
    public void enviarCorreo(String destinatario, String asunto, String contenido) throws IOException {
        Email from = new Email(remitente);
        Email to = new Email(destinatario);
        Content content = new Content("text/plain", contenido);
        Mail mail = new Mail(from, asunto, to, content);
        enviar(mail);
    }

    // Correo masivo
    public void enviarCorreoMasivo(String[] destinatarios, String asunto, String contenido) throws IOException {
        for (String destinatario : destinatarios) {
            enviarCorreo(destinatario, asunto, contenido);
        }
    }

    // Correos individuales
    public void enviarCorreosIndividuales(List<String> destinatarios, String asunto, String contenido) throws IOException {
        for (String destinatario : destinatarios) {
            enviarCorreo(destinatario, asunto, contenido);
        }
    }

    // Correo HTML
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) throws IOException {
        Email from = new Email(remitente);
        Email to = new Email(destinatario);
        Content content = new Content("text/html", contenidoHtml);
        Mail mail = new Mail(from, asunto, to, content);
        enviar(mail);
    }

    // Correo HTML masivo
    public void enviarCorreoHtmlMasivo(List<String> destinatarios, String asunto, String contenidoHtml) throws IOException {
        for (String destinatario : destinatarios) {
            enviarCorreoHtml(destinatario, asunto, contenidoHtml);
        }
    }

    // Plantilla HTML
    public String plantillaBienvenida() {
        return """
               <html>
                   <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                       <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;">
                           <h1 style="color: #4CAF50; text-align: center;">¡Bienvenido a ShenmyKappai!</h1>
                           <p>Estamos emocionados de que te unas a nuestra comunidad. Prepárate para aprender, crecer y alcanzar tus metas.</p>
                           <div style="text-align: center; margin-top: 20px;">
                               <a href="https://www.edugame.com"
                                  style="text-decoration: none; color: white; background-color: #4CAF50; padding: 10px 20px; border-radius: 5px;">
                                   ¡Comienza ahora!
                               </a>
                           </div>
                       </div>
                   </body>
               </html>
               """;
    }

    // Método interno para enviar
    private void enviar(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        System.out.println("Status: " + response.getStatusCode());
        if (response.getStatusCode() >= 400) {
            System.err.println("Error al enviar correo: " + response.getBody());
        }
    }
}
