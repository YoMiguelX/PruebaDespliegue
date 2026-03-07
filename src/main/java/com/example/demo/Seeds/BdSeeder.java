package com.example.demo.Seeds;

import com.example.demo.Model.Rol;
import com.example.demo.Model.Usuario;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BdSeeder  implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public BdSeeder(RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) {
            Rol admin = new Rol();
            admin.setIdRol(1);
            admin.setNombreRol("Administrador");
            rolRepository.save(admin);

            Rol user = new Rol();
            user.setIdRol(2);
            user.setNombreRol("Usuario");
            rolRepository.save(user);


            if (!usuarioRepository.existsByCorreoUsuario("admin@example.com")) {
                Usuario u = new Usuario();
                u.setNombreUsuario("Admin");
                u.setApellidoUsuario("System");
                u.setCorreoUsuario("admin@example.com");
                u.setContrasena(passwordEncoder.encode("AdminPass123!"));
                u.setRol(admin);
                usuarioRepository.save(u);
            }
        }
    }
}
