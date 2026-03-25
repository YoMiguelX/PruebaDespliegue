package com.example.demo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroUsuarioDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidoUsuario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    private String telefono;

    // 🆕 NUEVO CAMPO
    @NotBlank(message = "El gametag es obligatorio")
    @Size(min = 3, max = 20, message = "El gametag debe tener entre 3 y 20 caracteres")
    private String gametag;

    // Constructor por defecto
    public RegistroUsuarioDto() {}

    // Getters y Setters
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

    public String getGametag() { return gametag; }
    public void setGametag(String gametag) { this.gametag = gametag; }
}