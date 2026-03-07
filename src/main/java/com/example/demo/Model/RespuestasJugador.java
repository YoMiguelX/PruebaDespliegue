package com.example.demo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuestas_jugador")
public class RespuestasJugador {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuestasJugador;

    private String respuestasJugador;
    private Boolean correcta;
    private Integer tiempoRespuesta;

    @ManyToOne
    @JoinColumn(name = "idJugador", referencedColumnName = "idJugador")
    private Jugador jugador;


    @ManyToOne
    @JoinColumn(name = "PREGUNTAS_ID_PREGUNTAS")
    private Pregunta pregunta;

    // Getters y setters

    public Integer getIdRespuestasJugador() {
        return idRespuestasJugador;
    }

    public void setIdRespuestasJugador(Integer idRespuestasJugador) {
        this.idRespuestasJugador = idRespuestasJugador;
    }

    public String getRespuestasJugador() {
        return respuestasJugador;
    }

    public void setRespuestasJugador(String respuestasJugador) {
        this.respuestasJugador = respuestasJugador;
    }

    public Boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
    }

    public Integer getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
}
