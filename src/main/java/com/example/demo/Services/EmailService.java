package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    private final String remitente = "no-reply@gmail.com"; // remitente verificado en Brevo

    // Reset password
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "https://tu-frontend.com/reset-password?token=" + token;
        String contenido = "Hola,\n\nRecibimos una solicitud de restablecimiento de contraseña. " +
                "Usa este enlace para cambiar tu contraseña (válido 15 min):\n\n" + resetLink +
                "\n\nSi no pediste esto, ignora este correo.";
        enviarCorreo(to, "Restablecer contraseña - EduGame", contenido);
    }

    // Correo simple
    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        enviar(destinatario, asunto, "<html><body>" + contenido + "</body></html>");
    }

    // Correo HTML
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        enviar(destinatario, asunto, contenidoHtml);
    }

    // Correo masivo
    public void enviarCorreoMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        for (String destinatario : destinatarios) {
            enviar(destinatario, asunto, contenidoHtml);
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

    // Método interno para enviar usando Brevo API
    private void enviar(String destinatario, String asunto, String contenidoHtml) {
        String url = "https://api.brevo.com/v3/smtp/email";

        Map<String, Object> body = new HashMap<>();
        Map<String, String> sender = new HashMap<>();
        sender.put("email", remitente);

        Map<String, String> to = new HashMap<>();
        to.put("email", destinatario);

        body.put("sender", sender);
        body.put("to", List.of(to));
        body.put("subject", asunto);
        body.put("htmlContent", contenidoHtml);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody());
    }
}
