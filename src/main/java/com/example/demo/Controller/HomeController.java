package com.example.demo.Controller;

import com.example.demo.Model.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository individuoDao;

    // Página principal con listado


    // Formulario para registrar nuevo individuo
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("individuo", new Usuario());
        return "formulario"; // sin .html
    }
    @GetMapping("/")
    public String home() {
        return "home"; // busca templates/home.html
    }

    @GetMapping("/soporte")
    public String soporte() {
        return "soporte"; // busca templates/soporte.html
    }
}
