package com.example.demo.Repository;

import com.example.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoUsuario(String correoUsuario);
    Optional<Usuario> findByResetToken(String resetToken);

    @Override
    Optional<Usuario> findById(Integer integer);

    boolean existsByCorreoUsuario(String mail);


    // Verificar login (correo + contraseña)
    Optional<Usuario> findByCorreoUsuarioAndContrasena(String correoUsuario, String contrasena);

    // 🔎 Filtrar usuarios con criterios opcionales
    @Query("SELECT u FROM Usuario u " +
            "WHERE (:rol IS NULL OR u.rol.idRol = :rol) " +
            "AND (:nombre IS NULL OR u.nombreUsuario LIKE %:nombre%) " +
            "AND (:apellido IS NULL OR u.apellidoUsuario LIKE %:apellido%) " +
            "AND (:correo IS NULL OR u.correoUsuario LIKE %:correo%) " +
            "AND (:telefono IS NULL OR u.telUsuario LIKE %:telefono%)")
    List<Usuario> filtrar(
            @Param("rol") Integer rol,
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("correo") String correo,
            @Param("telefono") String telefono
    );


    // 🔴 Eliminar por ID (ya viene en JpaRepository)
    // → No es necesario definirlo, basta con usar deleteById(id).
    // Si quieres mantenerlo explícito:
    default void eliminar(Integer id) {
        deleteById(id);
    }

    // Buscar usuario por correo (versión alternativa si no quieres usar Optional)
    @Query("SELECT u FROM Usuario u WHERE u.correoUsuario = :correo")
    Usuario buscarPorCorreo(@Param("correo") String correo);


    List<Usuario> findByRol_IdRol(Integer idRol);


}
