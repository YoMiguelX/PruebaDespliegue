package com.example.demo.Controller;

import com.example.demo.Dto.DatoEstadisticoDto;
import com.example.demo.Services.ReporteJasperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReporteJasperController {

    private final ReporteJasperService reporteJasperService;

    public ReporteJasperController(ReporteJasperService reporteJasperService) {
        this.reporteJasperService = reporteJasperService;
    }

    @GetMapping("/api/reportes/jasper/estadistico")
    public void getReporteEstadistico(HttpServletResponse response) {
        try {
            List<DatoEstadisticoDto> datos = List.of(
                    new DatoEstadisticoDto("Enero",   123.45),
                    new DatoEstadisticoDto("Febrero",  98.76),
                    new DatoEstadisticoDto("Marzo",   150.00)
            );

            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Estadísticas de Ventas por Mes");
            params.put("AUTOR",  "Sistema Shenmi");

            // Ahora solo pasamos lista y params
            byte[] pdf = reporteJasperService.generarReporteEstadisticoPdf(datos, params);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte-estadistico.pdf");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();

        } catch (Exception e) {
            try {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error al generar reporte: "
                        + e.getMessage().replace("\"", "'") + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @GetMapping("/api/reportes/jasper/estadistico/base64")
    public ResponseEntity<Map<String, Object>> getReporteEstadisticoBase64() {
        try {
            List<DatoEstadisticoDto> datos = List.of(
                    new DatoEstadisticoDto("Enero",   123.45),
                    new DatoEstadisticoDto("Febrero",  98.76),
                    new DatoEstadisticoDto("Marzo",   150.00)
            );

            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Estadísticas de Ventas por Mes");

            // Ahora solo pasamos lista y params
            byte[] pdf = reporteJasperService.generarReporteEstadisticoPdf(datos, params);

            Map<String, Object> body = new HashMap<>();
            body.put("nombre", "reporte-estadistico.pdf");
            body.put("base64", Base64.getEncoder().encodeToString(pdf));
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al generar reporte: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
