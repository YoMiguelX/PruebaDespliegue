package com.example.demo.Controller;

import com.example.demo.Dto.DatoEstadisticoDto;
import com.example.demo.Services.ReporteJasperService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    new DatoEstadisticoDto("Enero", 123.45),
                    new DatoEstadisticoDto("Febrero", 98.76),
                    new DatoEstadisticoDto("Marzo", 150.00)
            );

            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Estadísticas de Ventas por Mes");
            params.put("AUTOR", "Sistema ShenmyKappai");

            byte[] pdf = reporteJasperService.generarReporteEstadisticoPdf(datos, params);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte-estadistico.pdf");
            response.setContentLength(pdf.length);

            try (var out = response.getOutputStream()) {
                out.write(pdf);
                out.flush();
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Error al generar reporte: "
                        + e.getMessage().replace("\"", "'") + "\"}");
            } catch (Exception ignored) {}
        }
    }
}
