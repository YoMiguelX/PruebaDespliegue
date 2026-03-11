package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Rutas que NO requieren autenticación
        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/registro") ||
                path.startsWith("/api/") || path.startsWith("/css/") ||
                path.startsWith("/js/") || path.startsWith("/images/")) {
            return true;
        }

        // Verificar sesión para TODAS las demás rutas
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}