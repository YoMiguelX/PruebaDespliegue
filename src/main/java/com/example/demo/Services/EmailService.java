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

    @Value("${BREVO_SENDER_EMAIL:no-reply@shenmykappai.com}")
    private String remitente;

    @Value("${BREVO_SENDER_NAME:ShenmyKappai}")
    private String remitenteNombre;

    // ─── Reset de contraseña ──────────────────────────────────────────────────

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String contenido = "Hola,<br><br>Recibimos una solicitud de restablecimiento de contraseña. "
                + "Usa este enlace para cambiar tu contraseña (válido 15 min):<br><br>"
                + "<a href=\"" + resetLink + "\">" + resetLink + "</a>"
                + "<br><br>Si no pediste esto, ignora este correo.";
        enviarCorreoHtml(to, "Restablecer contraseña - EduGame", contenido);
    }

    // ─── Correo texto plano (individual) ─────────────────────────────────────

    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        String html = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + contenido.replace("\n", "<br>")
                + "</body></html>";
        enviar(destinatario, asunto, html);
    }

    // ─── Correo HTML (individual) ─────────────────────────────────────────────

    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        enviar(destinatario, asunto, contenidoHtml);
    }

    // ─── Correo masivo texto plano — usado por CorreoController ──────────────

    public void enviarCorreosIndividuales(List<String> destinatarios, String asunto, String contenido) {
        String html = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + contenido.replace("\n", "<br>")
                + "</body></html>";
        for (String destinatario : destinatarios) {
            enviar(destinatario, asunto, html);
        }
    }

    // ─── Correo masivo HTML — usado por CorreoController ─────────────────────

    public void enviarCorreoHtmlMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        for (String destinatario : destinatarios) {
            enviar(destinatario, asunto, contenidoHtml);
        }
    }

    // ─── Alias de compatibilidad ──────────────────────────────────────────────

    public void enviarCorreoMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        enviarCorreoHtmlMasivo(destinatarios, asunto, contenidoHtml);
    }

    // ─── Plantilla HTML de bienvenida ─────────────────────────────────────────

    public String plantillaBienvenida() {
        return "<html>"
             + "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">"
             + "<div style=\"max-width:600px;margin:0 auto;padding:20px;border:1px solid #ddd;"
             + "border-radius:10px;background-color:#f9f9f9;\">"
             + "<h1 style=\"color:#4CAF50;text-align:center;\">¡Bienvenido a ShenmyKappai!</h1>"
             + "<p>Estamos emocionados de que te unas a nuestra comunidad.</p>"
             + "<div style=\"text-align:center;margin-top:20px;\">"
             + "<a href=\"https://www.edugame.com\" style=\"text-decoration:none;color:white;"
             + "background-color:#4CAF50;padding:10px 20px;border-radius:5px;\">¡Comienza ahora!</a>"
             + "</div></div></body></html>";
    }

    // ─── Método interno: llama Brevo REST API ─────────────────────────────────

    private void enviar(String destinatario, String asunto, String contenidoHtml) {
        String url = "https://api.brevo.com/v3/smtp/email";

        Map<String, Object> body = new HashMap<>();

        Map<String, String> sender = new HashMap<>();
        sender.put("email", remitente);
        sender.put("name", remitenteNombre);

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

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("[Brevo] Enviado a " + destinatario + " → Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("[Brevo] Error al enviar a " + destinatario + ": " + e.getMessage());
            throw new RuntimeException("Error al enviar correo via Brevo", e);
        }
    }
}
