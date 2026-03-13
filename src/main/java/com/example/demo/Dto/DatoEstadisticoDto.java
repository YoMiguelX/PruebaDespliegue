package com.example.demo.Dto;

public class DatoEstadisticoDto {
    private String categoria;
    private Double valor;

    public DatoEstadisticoDto(String categoria, Double valor) {
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
