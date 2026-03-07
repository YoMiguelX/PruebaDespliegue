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

    // --- Texto plano masivo ---
    @PostMapping("/masivo/texto")
    public ResponseEntity<String> enviarCorreoMasivoTexto(@RequestBody Map<String, Object> payload) {
        List<String> destinatarios = (List<String>) payload.get("destinatarios");
        String asunto = (String) payload.get("asunto");
        String contenido = (String) payload.get("contenido");

        emailService.enviarCorreosIndividuales(destinatarios, asunto, contenido);
        return ResponseEntity.ok("Correos de texto enviados correctamente");
    }

    // --- HTML masivo ---
    @PostMapping("/masivo/html")
    public ResponseEntity<String> enviarCorreoMasivoHtml(@RequestBody Map<String, Object> payload) {
        List<String> destinatarios = (List<String>) payload.get("destinatarios");
        String asunto = (String) payload.get("asunto");
        String contenidoHtml = (String) payload.get("contenidoHtml");

        emailService.enviarCorreoHtmlMasivo(destinatarios, asunto, contenidoHtml);
        return ResponseEntity.ok("Correos HTML enviados correctamente");
    }

    // --- HTML individual (ejemplo: bienvenida) ---
    @PostMapping("/bienvenida")
    public ResponseEntity<String> enviarCorreoBienvenida(@RequestParam String destinatario) {
        emailService.enviarCorreoHtml(destinatario,
                "¡Bienvenido a ShenmyKappai!",
                emailService.plantillaBienvenida()); // usa la plantilla del servicio
        return ResponseEntity.ok("Correo de bienvenida enviado a " + destinatario);
    }
}
