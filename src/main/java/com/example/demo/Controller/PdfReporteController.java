package com.example.demo.Controller;

import com.example.demo.Dto.DatoEstadisticoDto;
import com.example.demo.Services.PdfReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes/pdf")
public class PdfReporteController {

    private final PdfReporteService pdfReporteService;

    public PdfReporteController(PdfReporteService pdfReporteService) {
        this.pdfReporteService = pdfReporteService;
    }

    @GetMapping("/estadistico")
    public void getReporteEstadistico(HttpServletResponse response) {
        try {
            // Datos de ejemplo — reemplaza con tu consulta a BD
            List<DatoEstadisticoDto> datos = List.of(
                    new DatoEstadisticoDto("Enero",   123.45),
                    new DatoEstadisticoDto("Febrero",  98.76),
                    new DatoEstadisticoDto("Marzo",   150.00)
            );

            byte[] pdf = pdfReporteService.generarReporteEstadisticoPdf(
                    datos, "Estadísticas de Ventas por Mes");

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte-estadistico.pdf");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();

        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
            } catch (Exception ignored) {}
        }
    }
}