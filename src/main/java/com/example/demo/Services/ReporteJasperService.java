package com.example.demo.Services;

import com.example.demo.Dto.DatoEstadisticoDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReporteJasperService {

    /**
     * Genera un reporte estadístico en PDF usando JasperReports.
     *
     * @param lista   Lista de datos estadísticos (DTO con categoria y valor)
     * @param rutaJrxml Ruta del archivo jrxml dentro de resources (ej: "reportes/reporte_estadistico.jrxml")
     * @param params  Parámetros adicionales para el reporte (ej: TITULO, AUTOR, etc.)
     * @return        PDF en bytes
     */
    public byte[] generarReporteEstadisticoPdf(List<DatoEstadisticoDto> lista,
                                               String rutaJrxml,
                                               Map<String, Object> params) {
        try {
            // 1. Cargar la plantilla .jrxml desde resources
            InputStream reporteStream = getClass().getResourceAsStream("/" + rutaJrxml);
            if (reporteStream == null) {
                throw new RuntimeException("No se encontró el reporte: " + rutaJrxml);
            }

            // 2. Compilar el .jrxml a JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);

            // 3. Crear DataSource con la lista de DTOs
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

            // 4. Llenar el reporte con datos y parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // 5. Exportar a PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Error generando el reporte Jasper", e);
        }
    }
}
