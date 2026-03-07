package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // Rutas públicas
        if (uri.equals("/login") || uri.equals("/") || uri.startsWith("/registro")
                || uri.startsWith("/restablecer") || uri.startsWith("/CSS") || uri.startsWith("/JS")
                || uri.startsWith("/IMG")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean logeado = session != null && session.getAttribute("usuarioId") != null;


        // Rutas protegidas
        if (uri.startsWith("/admin") || uri.startsWith("/perfil") || uri.startsWith("/jugador")) {
            if (!logeado) {
                res.sendRedirect("/login?denied");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
