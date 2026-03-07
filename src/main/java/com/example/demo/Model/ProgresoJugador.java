package com.example.demo.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "progreso_jugador")
public class ProgresoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROGRESO_JUGADOR")
    private Integer id;

    @Column(name = "PUNTAJE_NIVEL")
    private Integer puntajeNivel;

    @Column(name = "nivel_completado")
    private Boolean nivelCompletado;

    @Column(name = "TIEMPO_JUGADO")
    private Integer tiempoJugado;

    @Column(name = "FECHA_COMPLETADO")
    private LocalDate fechaCompletado;

    @ManyToOne
    @JoinColumn(name = "NIVELES_ID_NIVELES")
    private Nivel nivel;

    @ManyToOne
    @JoinColumn(name = "ID_JUGADOR")
    private Jugador jugador;

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPuntajeNivel() { return puntajeNivel; }
    public void setPuntajeNivel(Integer puntajeNivel) { this.puntajeNivel = puntajeNivel; }

    public Boolean getNivelCompletado() { return nivelCompletado; }
    public void setNivelCompletado(Boolean nivelCompletado) { this.nivelCompletado = nivelCompletado; }

    public Integer getTiempoJugado() { return tiempoJugado; }
    public void setTiempoJugado(Integer tiempoJugado) { this.tiempoJugado = tiempoJugado; }

    public LocalDate getFechaCompletado() { return fechaCompletado; }
    public void setFechaCompletado(LocalDate fechaCompletado) { this.fechaCompletado = fechaCompletado; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
}
