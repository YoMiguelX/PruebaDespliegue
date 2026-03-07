package com.example.demo.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mundos")
public class Mundo {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MUNDOS")
    private Long id;
    @Column(name = "NOMBRE_MUNDO")
    private String nombre;

    @OneToMany(mappedBy = "mundo")
    private List<Nivel> niveles;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

}
