package com.example.demo.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class CertificadoController {

    // Mapa de cursos a categorías de OpenTrivia DB
    private static final Map<String, Integer> CURSO_CATEGORIA = Map.ofEntries(
            Map.entry("matematicas",    19),   // Mathematics
            Map.entry("fisica",         17),   // Science & Nature
            Map.entry("biologia",       17),   // Science & Nature
            Map.entry("quimica",        17),   // Science & Nature
            Map.entry("historia",       23),   // History
            Map.entry("geografia",      22),   // Geography
            Map.entry("ingles",         10),   // Entertainment: Books
            Map.entry("programacion",   18),   // Science: Computers
            Map.entry("logica",         19),   // Mathematics
            Map.entry("medio-ambiente", 17),   // Science & Nature
            Map.entry("comprar-casa",    9)    // General Knowledge
    );

    // Nombre bonito de cada curso
    private static final Map<String, String> CURSO_NOMBRE = Map.ofEntries(
            Map.entry("matematicas",    "Matemáticas"),
            Map.entry("fisica",         "Física"),
            Map.entry("biologia",       "Biología"),
            Map.entry("quimica",        "Química"),
            Map.entry("historia",       "Historia"),
            Map.entry("geografia",      "Geografía"),
            Map.entry("ingles",         "Inglés"),
            Map.entry("programacion",   "Programación"),
            Map.entry("logica",         "Lógica"),
            Map.entry("medio-ambiente", "Medio Ambiente"),
            Map.entry("comprar-casa",   "Módulo de Aprendizaje")
    );

    /**
     * GET /certificado?curso=matematicas
     * Página de trivia + certificado para un curso dado.
     */
    @GetMapping("/certificado")
    public String certificado(
            @RequestParam(required = false, defaultValue = "matematicas") String curso,
            HttpSession session,
            Model model) {

        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        // Obtener nombre del usuario de la sesión
        String nombreUsuario = (String) session.getAttribute("usuarioNombre");
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            nombreUsuario = "Estudiante";
        }

        // Categoría de OpenTrivia para este curso
        int categoria = CURSO_CATEGORIA.getOrDefault(curso, 9);

        // Nombre legible del curso
        String nombreCurso = CURSO_NOMBRE.getOrDefault(curso, "Curso General");

        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("curso",         curso);
        model.addAttribute("nombreCurso",   nombreCurso);
        model.addAttribute("categoriaId",   categoria);

        return "certificado";
    }
}
