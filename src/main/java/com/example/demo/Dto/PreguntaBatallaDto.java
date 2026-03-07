package com.example.demo.Dto;

import java.util.ArrayList;
import java.util.List;

public class PreguntaBatallaDto {
    private Integer id;
    private String textoPregunta;
    private List<String> opciones;  // Unity espera un array
    private String respuestaCorrecta;
    private String explicacion;
    private Integer puntos;
    private Integer nivelId;

    // Constructor vacío
    public PreguntaBatallaDto() {
        this.opciones = new ArrayList<>();
    }

    // Constructor desde entidad Pregunta
    public PreguntaBatallaDto(Integer id, String textoPregunta, String opcionesRespuesta,
                              String respuestaCorrecta, String explicacion, Integer puntos, Integer nivelId) {
        this.id = id;
        this.textoPregunta = textoPregunta;
        this.respuestaCorrecta = respuestaCorrecta;
        this.explicacion = explicacion;
        this.puntos = puntos;
        this.nivelId = nivelId;
        this.opciones = new ArrayList<>();

        // Parsear opcionesRespuesta con mejor manejo de errores
        // Formato esperado: "A) texto1|B) texto2|C) texto3|D) texto4"
        if (opcionesRespuesta != null && !opcionesRespuesta.isEmpty()) {
            try {
                String[] opcionesArray = opcionesRespuesta.split("\\|");
                for (String opcion : opcionesArray) {
                    if (opcion != null && !opcion.trim().isEmpty()) {
                        // Agregar cada opción completa (con su letra)
                        this.opciones.add(opcion.trim());
                    }
                }

                // Validar que tengamos 4 opciones
                if (this.opciones.size() != 4) {
                    System.err.println("⚠️ Pregunta ID " + id + " no tiene 4 opciones. Tiene: " + this.opciones.size());
                }
            } catch (Exception e) {
                System.err.println("❌ Error parseando opciones de pregunta ID " + id + ": " + e.getMessage());
            }
        } else {
            System.err.println("⚠️ Pregunta ID " + id + " no tiene opciones definidas");
        }
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTextoPregunta() { return textoPregunta; }
    public void setTextoPregunta(String textoPregunta) { this.textoPregunta = textoPregunta; }

    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getExplicacion() { return explicacion; }
    public void setExplicacion(String explicacion) { this.explicacion = explicacion; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }

    public Integer getNivelId() { return nivelId; }
    public void setNivelId(Integer nivelId) { this.nivelId = nivelId; }

    @Override
    public String toString() {
        return "PreguntaBatallaDto{" +
                "id=" + id +
                ", textoPregunta='" + textoPregunta + '\'' +
                ", opciones=" + opciones.size() +
                ", respuestaCorrecta='" + respuestaCorrecta + '\'' +
                ", puntos=" + puntos +
                '}';
    }
}