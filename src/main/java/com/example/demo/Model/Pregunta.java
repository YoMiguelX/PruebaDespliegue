package com.example.demo.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "preguntas")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPreguntas;

    @Column(columnDefinition = "TEXT")
    private String textoPregunta;

    private String opcionesRespuesta;
    private String respuestaCorrecta;



    @Column(columnDefinition = "TEXT")
    private String explicacion;

    private Integer puntos;

    @ManyToOne
    @JoinColumn(name = "NIVELES_ID_NIVELES")
    private Nivel nivel;

    @OneToMany(mappedBy = "pregunta")
    private List<RespuestasJugador> respuestasJugador;

    // Getters y setters
    public Integer getIdPreguntas() {
        return idPreguntas;
    }

    public void setIdPreguntas(Integer idPreguntas) {
        this.idPreguntas = idPreguntas;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public String getOpcionesRespuesta() {
        return opcionesRespuesta;
    }

    public void setOpcionesRespuesta(String opcionesRespuesta) {
        this.opcionesRespuesta = opcionesRespuesta;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<RespuestasJugador> getRespuestasJugador() {
        return respuestasJugador;
    }

    public void setRespuestasJugador(List<RespuestasJugador> respuestasJugador) {
        this.respuestasJugador = respuestasJugador;
    }
}
