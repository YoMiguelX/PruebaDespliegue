//package com.example.demo.config;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secretBase64;
//
//    @Value("${jwt.expiration-ms:86400000}")
//    private long expirationMs;
//
//    private Key key;
//
//    @PostConstruct
//    public void init() {
//        // Intentar decodificar como Base64 estándar y si falla, como Base64URL
//        byte[] decoded;
//        try {
//            decoded = Base64.getDecoder().decode(secretBase64);
//        } catch (IllegalArgumentException ex) {
//            decoded = Base64.getUrlDecoder().decode(secretBase64);
//        }
//        this.key = Keys.hmacShaKeyFor(decoded);
//    }
//
//    public String generarToken(String subjectCorreo, Integer rolId) {
//        return Jwts.builder()
//                .setSubject(subjectCorreo)
//                .claim("rol", rolId)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public Jws<Claims> validarToken(String token) throws JwtException {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token);
//    }
//
//    public Integer extraerRolId(String token) {
//        Claims claims = validarToken(token).getBody();
//        Object rol = claims.get("rol");
//        return rol == null ? null : (rol instanceof Integer ? (Integer) rol : Integer.valueOf(rol.toString()));
//    }
//
//    public String extraerCorreo(String token) {
//        return validarToken(token).getBody().getSubject();
//    }
//}
