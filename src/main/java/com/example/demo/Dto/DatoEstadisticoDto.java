package com.example.demo.Dto;

public class DatoEstadisticoDto {
    private String categoria;
    private Double valor;

    public DatoEstadisticoDto(String categoria, Double valor) {
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getCategoria() { return categoria; }
    public Double getValor() { return valor; }
}
