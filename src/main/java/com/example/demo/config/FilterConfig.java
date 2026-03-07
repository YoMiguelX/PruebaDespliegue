package com.example.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        FilterRegistrationBean<NoCacheFilter> reg = new FilterRegistrationBean<>(new NoCacheFilter());
        reg.addUrlPatterns("/admin/*", "/perfil/*", "/jugador/*");
        reg.setOrder(1);
        return reg;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> reg = new FilterRegistrationBean<>(new AuthFilter());
        reg.addUrlPatterns("/admin/*", "/perfil/*", "/jugador/*");
        reg.setOrder(2);
        return reg;
    }
}
