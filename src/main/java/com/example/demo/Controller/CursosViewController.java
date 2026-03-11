package com.example.demo.Controller;

import com.example.demo.Model.Mundo;
import com.example.demo.Services.MundoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CursosViewController {

    @Autowired
    private MundoService mundoService;

    // Página principal de categorías (protegida)
    @GetMapping("/categoria-cursos")
    public String categoriaCursos(HttpSession session, Model model) {
        // Verificar sesión
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        // Obtener lista de mundos para el filtro
        List<Mundo> mundos = mundoService.findAll();
        model.addAttribute("mundos", mundos);

        return "categoriaCursos";
    }

    // Páginas de cursos individuales (TODAS protegidas)
    @GetMapping("/curso/{nombreCurso}")
    public String verCurso(@PathVariable String nombreCurso, HttpSession session, Model model) {
        // Verificar sesión
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        // Validar que el curso existe
        List<String> cursosValidos = List.of(
                "matematicas", "fisica", "biologia", "quimica", "historia",
                "geografia", "ingles", "programacion", "logica", "medio-ambiente",
                "comprar-casa"
        );

        if (!cursosValidos.contains(nombreCurso)) {
            return "redirect:/categoria-cursos";
        }

        // Mapear nombres de URL a nombres de archivo
        String archivoCurso = switch (nombreCurso) {
            case "medio-ambiente" -> "medioAmbiente";
            case "comprar-casa" -> "moduloAprendizaje";
            default -> nombreCurso;
        };

        return "cursos/" + archivoCurso;
    }

    // Perfil (protegido)
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        // Agregar datos del usuario al modelo
        model.addAttribute("usuarioNombre", session.getAttribute("usuarioNombre"));
        model.addAttribute("usuarioCorreo", session.getAttribute("usuarioCorreo"));

        return "perfil";
    }
}