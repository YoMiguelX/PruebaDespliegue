package com.example.demo.Services;

import com.example.demo.Model.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorCorreo(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        String role;
        if (usuario.getRol().getIdRol() == 1) {
            role = "ROLE_administrador";
        } else {
            role = "ROLE_usuario";
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getCorreoUsuario())
                .password(usuario.getContrasena())
                .roles(role.replace("ROLE_", "")) // Builder no acepta ROLE_, lo agrega solo
                .build();
    }


}
