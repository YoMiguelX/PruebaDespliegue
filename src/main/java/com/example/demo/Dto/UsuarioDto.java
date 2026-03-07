package com.example.demo.Dto;

public class UsuarioDto {

    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String estado;
    private Integer rolId;
    public record PasswordResetRequestDto(String email) {}
    public record PasswordResetConfirmDto(String token, String newPassword) {}

    // Constructor vacío
    public UsuarioDto() {}

    // Constructor completo
    public UsuarioDto(Integer id, String nombre, String apellido, String correo, String telefono, String estado, Integer rolId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
        this.rolId = rolId;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }
}
