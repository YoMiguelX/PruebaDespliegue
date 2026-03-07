package com.example.demo.Controller;

import com.example.demo.Dto.RegistroUsuarioDto;
import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Interface.IUsuarioService;
import com.example.demo.Model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDto());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") RegistroUsuarioDto dto, Model model) {
        ApiResponse<UsuarioDto> respuesta = usuarioService.registrarUsuario(dto);

        if (respuesta.getHttpStatusCode() == 201) {
            return "redirect:/login?registro=exitoso";
        } else {
            if (respuesta.getMessage().contains("correo")) {
                return "redirect:/login?error=correoExistente";
            }
            model.addAttribute("error", respuesta.getMessage());
            return "registro";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // plantilla login.html
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // eliminar cookie JSESSIONID del navegador
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath(request.getContextPath() == null ? "/" : request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // cabeceras extra de seguridad (por si)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return "redirect:/login";
    }

}
