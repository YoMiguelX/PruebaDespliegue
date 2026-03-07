package com.example.demo.Controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Model.Rol;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.Services.RolService;

import com.example.demo.Services.ExcelExportService;
import com.example.demo.Services.UsuarioService;
import com.example.demo.Model.Usuario;
import com.example.demo.Dto.FiltroDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private RolService rolService;

    private final RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdministradorController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // ===== LISTAR =====
    @GetMapping("/lista")
    public String listarUsuarios(FiltroDTO filtros, Model model, HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        List<Usuario> administradores = usuarioService.filtrar(1,
                filtros.getNombre(), filtros.getApellido(), filtros.getCorreo(), filtros.getTelefono());

        List<Usuario> usuarios = usuarioService.filtrar(2,
                filtros.getNombre(), filtros.getApellido(), filtros.getCorreo(), filtros.getTelefono());

        model.addAttribute("administradores", administradores);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("filtros", filtros);

        return "admin/lista";
    }

    // ===== ELIMINAR =====
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id,
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/admin/lista";
    }

    // ===== CREAR ADMIN =====
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }
        model.addAttribute("usuario", new Usuario());
        return "admin/crear_admin";
    }

    @PostMapping("/guardar")
    public String guardarAdmin(@ModelAttribute("usuario") Usuario usuario,
                               RedirectAttributes redirectAttrs, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }
        try {
            Rol rolAdmin = rolRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("El rol 1 (Admin) no existe"));

            usuario.setRol(rolAdmin);
            usuario.setEstadoUsuario("Activo");
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

            usuarioService.guardarUsuario(usuario);

            redirectAttrs.addFlashAttribute("success", "Administrador registrado correctamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al registrar administrador: " + e.getMessage());
        }
        return "redirect:/admin/crear";
    }

    // ===== EDITAR =====
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id,
                                          Model model,
                                          RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }

        Usuario usuario = usuarioService.buscarPorIdEntity(id);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/admin/lista";
        }

        // Pasamos la entidad Usuario directamente
        model.addAttribute("usuario", usuario);

        // Lista de roles para el select
        model.addAttribute("roles", rolService.findAll());

        return "admin/editar";
    }

    // ===== ACTUALIZAR =====
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Integer id,
                                    @ModelAttribute("usuario") Usuario usuarioForm,
                                    RedirectAttributes redirectAttributes , HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }

        Usuario usuarioBD = usuarioService.buscarPorIdEntity(id); // <--- MÉTODO NECESARIO

        if (usuarioBD == null) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/admin/lista";
        }

        try {
            usuarioBD.setNombreUsuario(usuarioForm.getNombreUsuario());
            usuarioBD.setApellidoUsuario(usuarioForm.getApellidoUsuario());
            usuarioBD.setTelUsuario(usuarioForm.getTelUsuario());
            usuarioBD.setCorreoUsuario(usuarioForm.getCorreoUsuario());
            usuarioBD.setEstadoUsuario(usuarioForm.getEstadoUsuario());

            if (usuarioForm.getRol() != null && usuarioForm.getRol().getIdRol() != null) {
                Rol rol = rolService.findById(usuarioForm.getRol().getIdRol());
                usuarioBD.setRol(rol);
            }

            usuarioService.guardarUsuario(usuarioBD);

            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente.");
            return "redirect:/admin/lista";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/admin/lista";
        }
    }

    // ===== EXPORTAR EXCEL =====
    @GetMapping("/exportarExcel")
    public void exportarExcel(HttpServletResponse response,
                              @RequestParam(required = false) String nombre,
                              @RequestParam(required = false) String apellido,
                              @RequestParam(required = false) String correo,
                              @RequestParam(required = false) String telefono,
                              HttpSession session) throws IOException {

        if (session.getAttribute("usuarioId") == null) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        List<Usuario> todosLosUsuarios;

        if (nombre != null || apellido != null || correo != null || telefono != null) {
            todosLosUsuarios = usuarioService.filtrar(null, nombre, apellido, correo, telefono);
        } else {
            todosLosUsuarios = usuarioService.obtenerTodosLosUsuarios();
        }

        excelExportService.exportarUsuariosAExcel(todosLosUsuarios, response);
    }

    // ===== CAMBIAR CONTRASEÑA (ADMIN LOGUEADO) =====
    @GetMapping("/cambiar-password")
    public String mostrarCambiarPassword(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }
        return "admin/cambiar-password"; // Vista en templates/admin/cambiar-password.html
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestParam("passwordActual") String passwordActual,
                                  @RequestParam("passwordNueva") String passwordNueva,
                                  @RequestParam("passwordConfirmar") String passwordConfirmar,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            redirectAttributes.addFlashAttribute("error", "No hay sesión activa.");
            return "redirect:/login";
        }

        Usuario usuarioActual = usuarioService.buscarPorIdEntity(usuarioId);

        if (usuarioActual == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/cambiar-password";
        }

        // Validaciones de contraseña (igual que antes)
        if (!passwordEncoder.matches(passwordActual, usuarioActual.getContrasena())) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta");
            return "redirect:/admin/cambiar-password";
        }

        if (!passwordNueva.equals(passwordConfirmar)) {
            redirectAttributes.addFlashAttribute("error", "Las nuevas contraseñas no coinciden");
            return "redirect:/admin/cambiar-password";
        }

        if (passwordEncoder.matches(passwordNueva, usuarioActual.getContrasena())) {
            redirectAttributes.addFlashAttribute("error", "La nueva contraseña debe ser diferente a la actual");
            return "redirect:/admin/cambiar-password";
        }

        if (!esPasswordSegura(passwordNueva)) {
            redirectAttributes.addFlashAttribute("error", "La contraseña no cumple los requisitos de seguridad.");
            return "redirect:/admin/cambiar-password";
        }

        usuarioActual.setContrasena(passwordEncoder.encode(passwordNueva));
        usuarioService.guardarUsuario(usuarioActual);

        redirectAttributes.addFlashAttribute("mensaje", "Contraseña cambiada exitosamente");
        return "redirect:/admin/lista";
    }


    // ===== VALIDACIÓN DE CONTRASEÑA SEGURA =====
    private boolean esPasswordSegura(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneNumero = password.matches(".*[0-9].*");
        boolean tieneEspecial = password.matches(".*[^a-zA-Z0-9].*");

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
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


//correo
    @GetMapping("/correo")
    public String mostrarFormularioCorreo() {
        return "admin/enviar-correo"; // la vista Thymeleaf
    }
//reporte pdf
    @GetMapping("/reporte-estadistico")
    public String mostrarReporteEstadistico() {
        return "admin/reporte-estadistico";
        // busca en src/main/resources/templates/admin/reporte-estadistico.html
    }
}
