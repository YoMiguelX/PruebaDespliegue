//package com.example.demo.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = header.substring(7);
//        try {
//            String correo = jwtUtil.extraerCorreo(token);
//            Integer rolId = jwtUtil.extraerRolId(token);
//
//            String roleName = (rolId != null && rolId == 1) ? "ROLE_ADMIN" : "ROLE_USER";
//
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(
//                            correo,
//                            null,
//                            List.of(new SimpleGrantedAuthority(roleName))
//                    );
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        } catch (Exception ex) {
//            SecurityContextHolder.clearContext();
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
