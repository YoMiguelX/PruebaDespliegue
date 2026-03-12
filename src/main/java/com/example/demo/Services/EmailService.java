package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String remitente;

    @Value("${BREVO_SENDER_NAME:ShenmyKappai}")
    private String remitenteNombre;

    // ─── Reset de contraseña ──────────────────────────────────────────────────

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String html = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + "<p>Hola,</p>"
                + "<p>Recibimos una solicitud de restablecimiento de contraseña.<br>"
                + "Usa este enlace (válido 15 min):</p>"
                + "<p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>"
                + "<p>Si no pediste esto, ignora este correo.</p>"
                + "</body></html>";
        enviar(to, "Restablecer contraseña - EduGame", html);
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

    // ─── Correo masivo texto plano ────────────────────────────────────────────

    public void enviarCorreosIndividuales(List<String> destinatarios, String asunto, String contenido) {
        String html = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + contenido.replace("\n", "<br>")
                + "</body></html>";
        for (String destinatario : destinatarios) {
            enviar(destinatario, asunto, html);
        }
    }

    // ─── Correo masivo HTML ───────────────────────────────────────────────────

    public void enviarCorreoHtmlMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        for (String destinatario : destinatarios) {
            enviar(destinatario, asunto, contenidoHtml);
        }
    }

    public void enviarCorreoMasivo(List<String> destinatarios, String asunto, String contenidoHtml) {
        enviarCorreoHtmlMasivo(destinatarios, asunto, contenidoHtml);
    }

    // ─── Plantilla bienvenida ─────────────────────────────────────────────────

    public String plantillaBienvenida() {
        return "<html><body style=\"font-family:Arial,sans-serif;line-height:1.6;color:#333;\">"
             + "<div style=\"max-width:600px;margin:0 auto;padding:20px;border:1px solid #ddd;"
             + "border-radius:10px;background:#f9f9f9;\">"
             + "<h1 style=\"color:#4CAF50;text-align:center;\">¡Bienvenido a ShenmyKappai!</h1>"
             + "<p>Estamos emocionados de que te unas a nuestra comunidad.</p>"
             + "<div style=\"text-align:center;margin-top:20px;\">"
             + "<a href=\"https://www.edugame.com\" style=\"color:white;background:#4CAF50;"
             + "padding:10px 20px;border-radius:5px;text-decoration:none;\">¡Comienza ahora!</a>"
             + "</div></div></body></html>";
    }

    // ─── Método de diagnóstico: verifica config antes de enviar ──────────────

    public Map<String, String> diagnosticar() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("BREVO_API_KEY configurada", (apiKey != null && !apiKey.isBlank()) ? "SI (" + apiKey.length() + " caracteres)" : "NO ← FALTA");
        info.put("BREVO_SENDER_EMAIL", (remitente != null && !remitente.isBlank()) ? remitente : "NO CONFIGURADO ← FALTA");
        info.put("BREVO_SENDER_NAME", remitenteNombre);
        return info;
    }

    // ─── Método interno: llama Brevo REST API ─────────────────────────────────

    private void enviar(String destinatario, String asunto, String contenidoHtml) {

        // Validación previa para detectar config faltante
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("[Brevo] BREVO_API_KEY no está configurada en application.properties");
        }
        if (remitente == null || remitente.isBlank()) {
            throw new IllegalStateException("[Brevo] BREVO_SENDER_EMAIL no está configurada en application.properties");
        }

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
            System.out.println("[Brevo] ✓ Correo enviado a " + destinatario
                    + " | Status: " + response.getStatusCode()
                    + " | Body: " + response.getBody());

        } catch (HttpClientErrorException e) {
            // Error 4xx: API key inválida, remitente no verificado, etc.
            System.err.println("[Brevo] ✗ Error " + e.getStatusCode()
                    + " al enviar a " + destinatario
                    + " | Respuesta Brevo: " + e.getResponseBodyAsString());
            throw new RuntimeException("Brevo rechazó el correo (" + e.getStatusCode() + "): "
                    + e.getResponseBodyAsString(), e);

        } catch (HttpServerErrorException e) {
            // Error 5xx: falla del servidor de Brevo
            System.err.println("[Brevo] ✗ Error servidor " + e.getStatusCode()
                    + " | Respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error interno de Brevo (" + e.getStatusCode() + ")", e);

        } catch (Exception e) {
            // Error de red, timeout, etc.
            System.err.println("[Brevo] ✗ Error de conexión al enviar a " + destinatario + ": " + e.getMessage());
            throw new RuntimeException("No se pudo conectar con Brevo: " + e.getMessage(), e);
        }
    }
}
