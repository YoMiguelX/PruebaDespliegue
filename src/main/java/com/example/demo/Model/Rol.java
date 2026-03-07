package com.example.demo.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "NOMBRE_ROL")
    private String nombreRol;




    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }



    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }


}
