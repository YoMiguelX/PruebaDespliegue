package com.example.demo.Services;

import com.example.demo.Model.ResetToken;
import com.example.demo.Model.Usuario;
import com.example.demo.Repository.ResetTokenRepository;
import com.example.demo.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private final UsuarioRepository usuarioRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;   // ← Brevo en lugar de JavaMailSender

    public PasswordResetService(UsuarioRepository usuarioRepository,
                                ResetTokenRepository resetTokenRepository,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void createAndSendToken(String email) {
        usuarioRepository.findByCorreoUsuario(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            ResetToken rt = new ResetToken(token, user, Instant.now().plus(15, ChronoUnit.MINUTES));
            resetTokenRepository.save(rt);
            log.info("Reset token generado para {}", email);

            // Envío via Brevo
            emailService.sendPasswordResetEmail(email, token);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        ResetToken rt = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        if (rt.getExpiration().isBefore(Instant.now())) {
            resetTokenRepository.delete(rt);
            throw new IllegalArgumentException("Token expirado");
        }

        Usuario user = rt.getUsuario();
        user.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);
        resetTokenRepository.delete(rt);
        log.info("Contraseña actualizada para usuario id={}", user.getIdUsuario());
    }
}
