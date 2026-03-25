package com.example.demo.Interface;

import com.example.demo.Dto.RegistroUsuarioDto;
import com.example.demo.Dto.RegistroUsuarioDto;
import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Model.Usuario;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUsuarioService {
    ApiResponse<List<UsuarioDto>> findAll();

    List<Usuario> obtenerTodosLosAdmins();

    ApiResponse<UsuarioDto> findById(Integer id);


    Usuario loginWeb(String correo, String contrasena);

    @Transactional
    ApiResponse<UsuarioDto> registrarUsuario(RegistroUsuarioDto dto);

    ApiResponse<UsuarioDto> update(Integer id, UsuarioDto dto);

    ApiResponse<UsuarioDto> actualizarUsuario(UsuarioDto dto);

    void delete(Integer id);
    ApiResponse<UsuarioDto> verificarUsuario(String correo, String contrasena);
    ApiResponse<UsuarioDto> login(String correo, String contrasena);

    Usuario buscarPorCorreo(String correo);
    Usuario buscarPorToken(String token);
    Usuario guardarUsuario(Usuario usuario);

    List<Usuario> filtrar(Integer rol, String nombre, String apellido, String correo, String telefono);

    Usuario crearAdministrador(Usuario usuario);

    List<Usuario> obtenerTodosLosUsuarios();



    void cambiarPassword(Integer idUsuario, String passwordActual, String passwordNueva);


    ApiResponse<UsuarioDto> cambiarGametag(Integer usuarioId, String nuevoGametag);
}
