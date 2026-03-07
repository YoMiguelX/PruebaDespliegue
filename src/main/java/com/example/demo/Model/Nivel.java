package com.example.demo.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "niveles")
public class Nivel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNiveles;



    private String nombreNivel;
    private String descripcion;
    private Integer puntajeMinimo;
    private Integer puntajeMaximo;
    private Boolean completado;

    @ManyToOne
    @JoinColumn(name = "mundos_ID_MUNDOS")
    private Mundo mundo;

    @OneToMany(mappedBy = "nivel")
    private List<ProgresoJugador> progresos;

    @OneToMany(mappedBy = "nivel")
    private List<Pregunta> preguntas;

    // Getters y setters

    public Integer getIdNiveles() {
        return idNiveles;
    }

    public void setIdNiveles(Integer idNiveles) {
        this.idNiveles = idNiveles;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPuntajeMinimo() {
        return puntajeMinimo;
    }

    public void setPuntajeMinimo(Integer puntajeMinimo) {
        this.puntajeMinimo = puntajeMinimo;
    }

    public Integer getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(Integer puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public Mundo getMundo() {
        return mundo;
    }

    public void setMundo(Mundo mundo) {
        this.mundo = mundo;
    }

    public List<ProgresoJugador> getProgresos() {
        return progresos;
    }

    public void setProgresos(List<ProgresoJugador> progresos) {
        this.progresos = progresos;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
