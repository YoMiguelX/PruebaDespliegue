package com.example.demo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public class RegistroUsuarioDto {
    @NotNull
    private Integer rolId;
    @NotBlank private String nombreUsuario;
    @NotBlank private String apellidoUsuario;
    @Email private String correo;
    @NotBlank private String contrasena;
    @Size(min = 7, max = 15) private String telefono;

    public RegistroUsuarioDto() {}

    public RegistroUsuarioDto(Integer rolId, String nombreUsuario, String apellidoUsuario,
                              String correo, String contrasena, String telefono) {
        this.rolId = rolId;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getApellidoUsuario() { return apellidoUsuario; }
    public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}


