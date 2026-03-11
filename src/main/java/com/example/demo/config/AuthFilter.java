package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        // ── Recursos estáticos y rutas 100 % públicas ──────────────────────
        if (uri.equals("/")
                || uri.startsWith("/CSS")
                || uri.startsWith("/JS")
                || uri.startsWith("/IMG")
                || uri.startsWith("/favicon")
                || uri.startsWith("/registro")
                || uri.startsWith("/restablecer")
                || uri.startsWith("/nueva-contrasena")
                || uri.startsWith("/error")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean logeado = session != null && session.getAttribute("usuarioId") != null;

        // ── Si ya está logueado y llega al login → redirigir a su panel ────
        if (uri.equals("/login") && logeado) {
            Object rol = session.getAttribute("rol");
            if (Integer.valueOf(1).equals(rol)) {
                res.sendRedirect("/admin/lista");
            } else {
                res.sendRedirect("/usuario/perfil");
            }
            return;
        }

        // ── Rutas que sí requieren sesión ───────────────────────────────────
        boolean rutaProtegida =
                uri.startsWith("/admin")
                        || uri.startsWith("/usuario")
                        || uri.startsWith("/perfil")
                        || uri.startsWith("/jugador")
                        || uri.startsWith("/categoria-cursos")
                        || uri.startsWith("/curso")
                        || uri.startsWith("/quiz")
                        || uri.startsWith("/api/reportes");

        if (rutaProtegida && !logeado) {
            res.sendRedirect("/login?denied");
            return;
        }

        chain.doFilter(request, response);
    }
}