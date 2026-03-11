package com.example.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * NoCacheFilter: aplica cabeceras no-cache a todas las páginas protegidas,
     * impidiendo que el navegador muestre páginas viejas con el botón "atrás".
     */
    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        FilterRegistrationBean<NoCacheFilter> reg = new FilterRegistrationBean<>(new NoCacheFilter());
        reg.addUrlPatterns(
                "/admin/*",
                "/usuario/*",
                "/perfil/*",
                "/jugador/*",
                "/categoria-cursos",
                "/categoria-cursos/*",
                "/curso/*",
                "/quiz/*",
                "/login"        // evita que el login quede en caché tras cerrar sesión
        );
        reg.setOrder(1);
        return reg;
    }

    /**
     * AuthFilter: corre en TODAS las rutas para evaluar acceso.
     * Las rutas públicas se dejan pasar; las protegidas redirigen a /login si no hay sesión.
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> reg = new FilterRegistrationBean<>(new AuthFilter());
        reg.addUrlPatterns("/*");   // evalúa cada petición
        reg.setOrder(2);
        return reg;
    }
}