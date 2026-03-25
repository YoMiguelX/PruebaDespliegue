package com.example.demo.Controller;

import com.example.demo.Dto.LoginDto;
import com.example.demo.Dto.RegistroUsuarioDto;
import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Interface.IUsuarioService;
import com.example.demo.Services.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ApiResponse<List<UsuarioDto>> listar() {
        return usuarioService.findAll();
    }

    @PostMapping
    public ApiResponse<UsuarioDto> registrar(@Valid @RequestBody RegistroUsuarioDto dto) {
        return usuarioService.registrarUsuario(dto);
    }

    @PostMapping("/login")
    public ApiResponse<UsuarioDto> login(@RequestBody LoginDto dto) {
        return usuarioService.login(dto.getCorreo(), dto.getContrasena());
    }

    @GetMapping("/id/{id}")
    public ApiResponse<UsuarioDto> obtener(@PathVariable Integer id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<UsuarioDto> actualizar(@PathVariable Integer id, @RequestBody UsuarioDto dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.delete(id);
    }

    // ✅ ENDPOINT PARA CAMBIAR GAMETAG
    @PutMapping("/cambiar-gametag")
    public ApiResponse<UsuarioDto> cambiarGametag(
            @RequestParam Integer usuarioId,
            @RequestParam String nuevoGametag) {

        return usuarioService.cambiarGametag(usuarioId, nuevoGametag);
    }

    // ✅ CLASE INTERNA PARA RESETEO DE CONTRASEÑA - CORREGIDA
    @RestController
    @RequestMapping("/auth")
    public class PasswordResetController {

        private final PasswordResetService resetService;

        public PasswordResetController(PasswordResetService resetService) {
            this.resetService = resetService;
        }

        // ✅ CORREGIDO: usar getEmail()
        @PostMapping("/password-reset-request")
        public ResponseEntity<?> requestReset(@RequestBody UsuarioDto.PasswordResetRequestDto dto) {
            resetService.createAndSendToken(dto.getEmail());
            return ResponseEntity.ok(Map.of("message", "Si el correo existe, se envió el enlace de restablecimiento"));
        }

        // ✅ CORREGIDO: usar getToken() y getNewPassword()
        @PostMapping("/password-reset-confirm")
        public ResponseEntity<?> confirmReset(@RequestBody UsuarioDto.PasswordResetConfirmDto dto) {
            resetService.resetPassword(dto.getToken(), dto.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada"));
        }
    }
}