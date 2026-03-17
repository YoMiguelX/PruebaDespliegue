package com.example.demo.Controller;

import com.example.demo.Services.PdfReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            byte[] pdf = pdfReporteService.generarReporteCompleto();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte-shenmykappai.pdf");
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