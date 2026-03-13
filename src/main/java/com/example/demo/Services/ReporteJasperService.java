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

    /**
     * Genera un reporte estadístico en PDF usando JasperReports.
     *
     * @param lista    Lista de datos estadísticos (DTO con categoria y valor)
     * @param params   Parámetros adicionales para el reporte (ej: TITULO, AUTOR, etc.)
     * @return         PDF en bytes
     */
    public byte[] generarReporteEstadisticoPdf(List<DatoEstadisticoDto> lista,
                                               Map<String, Object> params) {
        try {
            InputStream reporteStream = getClass().getResourceAsStream("/Reports/ReporteEstadistico.jasper");
            if (reporteStream == null) {
                throw new RuntimeException("No se encontró el reporte en resources: /Reports/ReporteEstadistico.jasper");
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporteStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Error generando el reporte Jasper", e);
        }
    }

}
