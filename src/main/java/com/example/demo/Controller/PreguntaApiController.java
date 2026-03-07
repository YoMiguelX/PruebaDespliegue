package com.example.demo.Controller;

import com.example.demo.Dto.PreguntaBatallaDto;
import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Model.Pregunta;
import com.example.demo.Repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/batalla")
@CrossOrigin(origins = "*")
public class PreguntaApiController {

    @Autowired
    private PreguntaRepository preguntaRepository;

    /**
     * Obtener preguntas aleatorias para batalla
     * GET /api/batalla/preguntas/{nivelId}?cantidad=5
     */
    @GetMapping("/preguntas/{nivelId}")
    public ApiResponse<List<PreguntaBatallaDto>> getPreguntasBatalla(
            @PathVariable Integer nivelId,
            @RequestParam(defaultValue = "5") int cantidad) {

        ApiResponse<List<PreguntaBatallaDto>> response = new ApiResponse<>();

        try {
            // Validaciones
            if (nivelId == null || nivelId <= 0) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("El ID del nivel es inválido");
                return response;
            }

            if (cantidad <= 0 || cantidad > 20) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("La cantidad debe estar entre 1 y 20");
                return response;
            }

            // Verificar cuántas preguntas hay disponibles
            long totalDisponibles = preguntaRepository.countByNivelId(nivelId);

            if (totalDisponibles == 0) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No hay preguntas disponibles para el nivel " + nivelId);
                return response;
            }

            // Ajustar cantidad si hay menos preguntas disponibles
            int cantidadFinal = (int) Math.min(cantidad, totalDisponibles);

            // Obtener preguntas aleatorias
            List<Pregunta> preguntas = preguntaRepository.findRandomByNivelId(nivelId, cantidadFinal);

            if (preguntas.isEmpty()) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Error al obtener preguntas del nivel " + nivelId);
                return response;
            }

            // Convertir a DTO
            List<PreguntaBatallaDto> preguntasDto = preguntas.stream()
                    .map(p -> new PreguntaBatallaDto(
                            p.getIdPreguntas(),
                            p.getTextoPregunta(),
                            p.getOpcionesRespuesta(),
                            p.getRespuestaCorrecta(),
                            p.getExplicacion(),
                            p.getPuntos(),
                            p.getNivel().getIdNiveles()))
                    .collect(Collectors.toList());

            // Respuesta exitosa
            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.OK.value());
            response.setMessage("Preguntas obtenidas correctamente");
            response.setData(preguntasDto);
            response.setTotalRecords(preguntasDto.size());

            System.out.println("✅ Enviadas " + preguntasDto.size() + " preguntas del nivel " + nivelId);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al obtener preguntas: " + e.getMessage());

            System.err.println("❌ Error en getPreguntasBatalla: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Validar una respuesta del jugador
     * POST /api/batalla/validar
     * Body: { "preguntaId": 1, "respuesta": "A" }
     */
    @PostMapping("/validar")
    public ApiResponse<ValidarRespuestaResponse> validarRespuesta(
            @RequestBody ValidarRespuestaRequest request) {

        ApiResponse<ValidarRespuestaResponse> response = new ApiResponse<>();

        try {
            // Validaciones
            if (request.getPreguntaId() == null) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("El ID de la pregunta es requerido");
                return response;
            }

            if (request.getRespuesta() == null || request.getRespuesta().trim().isEmpty()) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("La respuesta es requerida");
                return response;
            }

            // Buscar pregunta
            Pregunta pregunta = preguntaRepository.findById(request.getPreguntaId())
                    .orElse(null);

            if (pregunta == null) {
                response.setSuccess(false);
                response.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Pregunta no encontrada");
                return response;
            }

            // Validar respuesta (case insensitive)
            String respuestaUsuario = request.getRespuesta().trim().toUpperCase();
            String respuestaCorrecta = pregunta.getRespuestaCorrecta().trim().toUpperCase();
            boolean esCorrecta = respuestaUsuario.equals(respuestaCorrecta);

            // Crear respuesta
            ValidarRespuestaResponse validacion = new ValidarRespuestaResponse();
            validacion.setCorrecta(esCorrecta);
            validacion.setRespuestaCorrecta(pregunta.getRespuestaCorrecta());
            validacion.setExplicacion(pregunta.getExplicacion());
            validacion.setPuntosGanados(esCorrecta ? pregunta.getPuntos() : 0);

            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.OK.value());
            response.setMessage(esCorrecta ? "¡Respuesta correcta!" : "Respuesta incorrecta");
            response.setData(validacion);

            System.out.println((esCorrecta ? "✅" : "❌") + " Pregunta " + request.getPreguntaId() +
                    " - Usuario: " + respuestaUsuario + " - Correcta: " + respuestaCorrecta);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al validar respuesta: " + e.getMessage());

            System.err.println("❌ Error en validarRespuesta: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Obtener estadísticas de un nivel
     * GET /api/batalla/nivel/{nivelId}/stats
     */
    @GetMapping("/nivel/{nivelId}/stats")
    public ApiResponse<NivelStatsResponse> getNivelStats(@PathVariable Integer nivelId) {

        ApiResponse<NivelStatsResponse> response = new ApiResponse<>();

        try {
            long totalPreguntas = preguntaRepository.countByNivelId(nivelId);

            NivelStatsResponse stats = new NivelStatsResponse();
            stats.setNivelId(nivelId);
            stats.setTotalPreguntas((int) totalPreguntas);
            stats.setDisponible(totalPreguntas > 0);

            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.OK.value());
            response.setMessage("Estadísticas obtenidas");
            response.setData(stats);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    // ============================================
    // CLASES INTERNAS PARA REQUESTS Y RESPONSES
    // ============================================

    /**
     * Request para validar respuesta
     */
    public static class ValidarRespuestaRequest {
        private Integer preguntaId;
        private String respuesta;

        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }

        public String getRespuesta() { return respuesta; }
        public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    }

    /**
     * Response de validación
     */
    public static class ValidarRespuestaResponse {
        private boolean correcta;
        private String respuestaCorrecta;
        private String explicacion;
        private Integer puntosGanados;

        public boolean isCorrecta() { return correcta; }
        public void setCorrecta(boolean correcta) { this.correcta = correcta; }

        public String getRespuestaCorrecta() { return respuestaCorrecta; }
        public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

        public String getExplicacion() { return explicacion; }
        public void setExplicacion(String explicacion) { this.explicacion = explicacion; }

        public Integer getPuntosGanados() { return puntosGanados; }
        public void setPuntosGanados(Integer puntosGanados) { this.puntosGanados = puntosGanados; }
    }

    /**
     * Response de stats del nivel
     */
    public static class NivelStatsResponse {
        private Integer nivelId;
        private Integer totalPreguntas;
        private boolean disponible;

        public Integer getNivelId() { return nivelId; }
        public void setNivelId(Integer nivelId) { this.nivelId = nivelId; }

        public Integer getTotalPreguntas() { return totalPreguntas; }
        public void setTotalPreguntas(Integer totalPreguntas) { this.totalPreguntas = totalPreguntas; }

        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }
}