package com.example.demo.Controller;

import com.example.demo.Dto.DatoEstadisticoDto;
import com.example.demo.Services.ReporteJasperService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Solo ADMIN puede generar reportes

    @GetMapping("/api/reportes/jasper/estadistico")
    public ResponseEntity<Map<String, Object>> getReporteEstadistico() {
        // 1. Datos de ejemplo (normalmente vendrían de la BD)
        List<DatoEstadisticoDto> datos = List.of(
                new DatoEstadisticoDto("Enero", 123.45),
                new DatoEstadisticoDto("Febrero", 98.76),
                new DatoEstadisticoDto("Marzo", 150.00)
        );

        // 2. Parámetros del reporte (puedes usarlos en el jrxml)
        Map<String, Object> params = new HashMap<>();
        params.put("TITULO", "Estadísticas de Ventas por Mes");
        params.put("AUTOR", "Sistema Shenmi");

        // 3. Generar PDF con Jasper
        byte[] pdf = reporteJasperService.generarReporteEstadisticoPdf(
                datos,
                "Reports/ReporteEstadistico.jrxml", // ruta dentro de resources
                params
        );

        // 4. Convertir a Base64 para enviar como JSON
        String base64 = Base64.getEncoder().encodeToString(pdf);

        // 5. Construir respuesta
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "reporte-estadistico-jasper.pdf");
        body.put("base64", base64);

        return ResponseEntity.ok(body);
    }
}
