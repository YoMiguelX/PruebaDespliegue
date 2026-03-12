package com.example.demo.Controller;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.Model.Usuario;
import com.example.demo.Services.EmailService;
import com.example.demo.Services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
@RequestMapping("/restablecer")
public class RestablecerController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String mostrarFormulario() {
        return "restablecer"; // templates/restablecer.html
    }

    @PostMapping("/enviar")
    public String enviarEnlace(@RequestParam("email") String email, Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorCorreo(email);
        if (usuario == null) {
            model.addAttribute("msg", "No existe una cuenta con ese correo.");
            return "restablecer";
        }

        String token = usuarioService.generarToken(usuario); // usa tu servicio
        String enlace = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + "/restablecer/nueva?token=" + token;

        try {
            emailService.enviarCorreo(usuario.getCorreoUsuario(),
                    "Restablecer contraseña - Shenmi",
                    "Hola " + usuario.getNombreUsuario() + ",\n\n" +
                            "Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace:\n" +
                            enlace + "\n\nEste enlace expirará en 1 hora.");
            model.addAttribute("msg", "Hemos enviado un enlace a tu correo.");
        } catch (Exception e) {
            model.addAttribute("msg", "Error al enviar el correo. Contacta al administrador.");
            // El error real aparecerá en los logs del servidor
        }
        return "restablecer";
    }

    @GetMapping("/nueva")
    public String mostrarNueva(@RequestParam("token") String token, Model model) {
        Usuario usuario = usuarioService.buscarPorToken(token);
        if (usuario == null) {
            model.addAttribute("msg", "El enlace no es válido o expiró.");
            return "restablecer";
        }
        model.addAttribute("token", token);
        return "nueva_contrasena";
    }

    @PostMapping("/nueva")
    public String guardarNueva(@RequestParam("token") String token,
                               @RequestParam("password") String password,
                               @RequestParam("password2") String password2,
                               Model model) {
        Usuario usuario = usuarioService.buscarPorToken(token);
        if (usuario == null) {
            model.addAttribute("msg", "El enlace no es válido o expiró.");
            return "restablecer";
        }

        if (!password.equals(password2)) {
            model.addAttribute("msg", "Las contraseñas no coinciden.");
            model.addAttribute("token", token);
            return "nueva_contrasena";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(password));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiration(null);
        usuarioService.guardarUsuario(usuario);

        model.addAttribute("msg", "Tu contraseña fue restablecida. Ahora puedes iniciar sesión.");
        return "login";
    }
}
