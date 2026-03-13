package com.example.demo.Services;

import com.example.demo.Dto.DatoEstadisticoDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReporteJasperService {

    public byte[] generarReporteEstadisticoPdf(List<DatoEstadisticoDto> lista,
                                               Map<String, Object> params) {
        try (InputStream reporteStream = getClass().getResourceAsStream("/Reports/ReporteEstadistico.jasper")) {
            if (reporteStream == null) {
                throw new IllegalStateException("No se encontró el archivo: /Reports/ReporteEstadistico.jasper");
            }

            // Cargar el reporte compilado (.jasper)
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporteStream);

            // Fuente de datos con la lista de DTOs
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

            // Llenar el reporte con datos y parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Exportar a PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Error generando el reporte Jasper", e);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando el archivo Jasper", e);
        }
    }
}
