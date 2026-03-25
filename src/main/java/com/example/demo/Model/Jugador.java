package com.example.demo.Model;

import com.example.demo.Model.BaseModel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "jugador")
public class Jugador extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idJugador;

    private String nombre;
    private LocalDate fechaRegistro;
    private LocalDate ultimaConexion;
    private String estado;

    // ✅ CORREGIDO: Usar el nombre correcto de la columna en BD
    @ManyToOne
    @JoinColumn(name = "usuario_ID_USUARIO", referencedColumnName = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    // ✅ CORREGIDO: Este campo es INT en BD, no es relación
    // En tu BD es "progreso" de tipo INT, no es una entidad
    @Column(name = "progreso")
    private Integer progreso;

    // ✅ Este es el campo para la relación con ProgresoJugador
    @ManyToOne
    @JoinColumn(name = "PROGRESO_JUGADOR_ID_PROGRESO_JUGADOR")
    private ProgresoJugador progresoJugador;

    @OneToMany(mappedBy = "jugador")
    private List<RespuestasJugador> respuestas;

    // Getters y setters
    public Integer getIdJugador() { return idJugador; }
    public void setIdJugador(Integer idJugador) { this.idJugador = idJugador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDate getUltimaConexion() { return ultimaConexion; }
    public void setUltimaConexion(LocalDate ultimaConexion) { this.ultimaConexion = ultimaConexion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getProgreso() { return progreso; }
    public void setProgreso(Integer progreso) { this.progreso = progreso; }

    public ProgresoJugador getProgresoJugador() { return progresoJugador; }
    public void setProgresoJugador(ProgresoJugador progresoJugador) { this.progresoJugador = progresoJugador; }

    public List<RespuestasJugador> getRespuestas() { return respuestas; }
    public void setRespuestas(List<RespuestasJugador> respuestas) { this.respuestas = respuestas; }
}