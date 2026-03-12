package com.example.demo.Controller;

import com.example.demo.Services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/correos")
public class CorreoController {

    private final EmailService emailService;

    public CorreoController(EmailService emailService) {
        this.emailService = emailService;
    }

    // ─── Diagnóstico: verifica config y hace envío de prueba ─────────────────

    @GetMapping("/diagnostico")
    public ResponseEntity<Map<String, String>> diagnostico() {
        return ResponseEntity.ok(emailService.diagnosticar());
    }

    @PostMapping("/prueba")
    public ResponseEntity<String> enviarPrueba(@RequestParam String destinatario) {
        try {
            emailService.enviarCorreo(
                    destinatario,
                    "Prueba de correo - Brevo",
                    "Este es un correo de prueba enviado desde ShenmyKappai.\nSi lo recibiste, Brevo está configurado correctamente."
            );
            return ResponseEntity.ok("✓ Correo de prueba enviado a " + destinatario + ". Revisa tu bandeja (y spam).");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("✗ Error al enviar: " + e.getMessage());
        }
    }

    // ─── Correo masivo texto plano ────────────────────────────────────────────

    @PostMapping("/masivo/texto")
    public ResponseEntity<String> enviarCorreoMasivoTexto(@RequestBody Map<String, Object> payload) {
        try {
            List<String> destinatarios = (List<String>) payload.get("destinatarios");
            String asunto = (String) payload.get("asunto");
            String contenido = (String) payload.get("contenido");
            emailService.enviarCorreosIndividuales(destinatarios, asunto, contenido);
            return ResponseEntity.ok("Correos de texto enviados correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al enviar correos: " + e.getMessage());
        }
    }

    // ─── Correo masivo HTML ───────────────────────────────────────────────────

    @PostMapping("/masivo/html")
    public ResponseEntity<String> enviarCorreoMasivoHtml(@RequestBody Map<String, Object> payload) {
        try {
            List<String> destinatarios = (List<String>) payload.get("destinatarios");
            String asunto = (String) payload.get("asunto");
            String contenidoHtml = (String) payload.get("contenidoHtml");
            emailService.enviarCorreoHtmlMasivo(destinatarios, asunto, contenidoHtml);
            return ResponseEntity.ok("Correos HTML enviados correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al enviar correos: " + e.getMessage());
        }
    }

    // ─── Correo de bienvenida ─────────────────────────────────────────────────

    @PostMapping("/bienvenida")
    public ResponseEntity<String> enviarCorreoBienvenida(@RequestParam String destinatario) {
        try {
            emailService.enviarCorreoHtml(
                    destinatario,
                    "¡Bienvenido a ShenmyKappai!",
                    emailService.plantillaBienvenida()
            );
            return ResponseEntity.ok("Correo de bienvenida enviado a " + destinatario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al enviar bienvenida: " + e.getMessage());
        }
    }
}
