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

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_PROGRESO_JUGADOR", referencedColumnName = "ID_PROGRESO_JUGADOR")
    private ProgresoJugador progreso;

    @OneToMany(mappedBy = "jugador")
    private List<RespuestasJugador> respuestas;

    //  Getters y setters
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

    public ProgresoJugador getProgreso() { return progreso; }
    public void setProgreso(ProgresoJugador progreso) { this.progreso = progreso; }

    public List<RespuestasJugador> getRespuestas() { return respuestas; }
    public void setRespuestas(List<RespuestasJugador> respuestas) { this.respuestas = respuestas; }
}
