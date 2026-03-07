package com.example.demo.Controller;

import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Model.Usuario;
import com.example.demo.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class PerfilController {


    @Autowired
    private UsuarioService usuarioService;

    // Mostrar página de perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Recargar datos actualizados desde la BD
        Usuario usuarioActualizado = usuarioService.buscarPorIdEntity(usuario.getIdUsuario());

        if (usuarioActualizado == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuarioActualizado);
        return "perfil";
    }

    // Actualizar información personal
    @PostMapping("/actualizar-info")
    public String actualizarInfo(
            @RequestParam String nombreUsuario,
            @RequestParam String apellidoUsuario,
            @RequestParam String correoUsuario,
            @RequestParam(required = false) String telUsuario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            // Validar teléfono si se proporcionó
            if (telUsuario != null && !telUsuario.trim().isEmpty()) {
                telUsuario = telUsuario.trim();

                // Validar que solo contenga números
                if (!telUsuario.matches("\\d+")) {
                    redirectAttributes.addFlashAttribute("error", "El teléfono solo debe contener números");
                    return "redirect:/usuario/perfil";
                }

                // Validar longitud exacta de 10 dígitos
                if (telUsuario.length() != 10) {
                    redirectAttributes.addFlashAttribute("error", "El teléfono debe tener exactamente 10 dígitos");
                    return "redirect:/usuario/perfil";
                }
            }

            // Crear DTO con los datos actualizados
            UsuarioDto dto = new UsuarioDto();
            dto.setId(usuario.getIdUsuario());
            dto.setNombre(nombreUsuario);
            dto.setApellido(apellidoUsuario);
            dto.setCorreo(correoUsuario);
            dto.setTelefono(telUsuario);
            dto.setEstado(usuario.getEstadoUsuario());
            dto.setRolId(usuario.getRol().getIdRol());

            // Actualizar en la BD
            usuarioService.update(usuario.getIdUsuario(), dto);

            // Actualizar la sesión con los nuevos datos
            Usuario usuarioActualizado = usuarioService.buscarPorIdEntity(usuario.getIdUsuario());
            session.setAttribute("usuario", usuarioActualizado);
            session.setAttribute("correo", usuarioActualizado.getCorreoUsuario());

            redirectAttributes.addFlashAttribute("mensaje", "Información actualizada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }

        return "redirect:/usuario/perfil";
    }
    // Cambiar contraseña
    @PostMapping("/cambiar-password")
    public String cambiarPassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirm,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            // Verificar que las contraseñas nuevas coincidan
            if (!passwordNueva.equals(passwordConfirm)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/usuario/perfil";
            }

            // Validar requisitos de contraseña
            if (!passwordNueva.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                redirectAttributes.addFlashAttribute("error",
                        "La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, minúscula y número");
                return "redirect:/usuario/perfil";
            }

            // Cambiar la contraseña
            usuarioService.cambiarPassword(usuario.getIdUsuario(), passwordActual, passwordNueva);

            redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuario/perfil";
    }

    // Verificar sesión
    @GetMapping("/check-session")
    @ResponseBody
    public String checkSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "NO_SESSION";
        }
        return "OK";
    }
}