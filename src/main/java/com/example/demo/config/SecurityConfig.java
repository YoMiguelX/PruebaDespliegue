package com.example.demo.config;

import com.example.demo.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
//
//    private final CustomUserDetailsService userDetailsService;
//    private final JwtAuthFilter jwtAuthFilter;
//
//    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthFilter = jwtAuthFilter;
//    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/**").authenticated() // API con JWT
//                        .requestMatchers("/", "/login", "/registro", "/IMG/**", "/CSS/**", "/JS/**").permitAll() // Thymeleaf
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .usernameParameter("correo")
//                        .passwordParameter("contrasena")
//                        .defaultSuccessUrl("/perfil", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout.permitAll());
//
//        return http.build();
//    }
}
