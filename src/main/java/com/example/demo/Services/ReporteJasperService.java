package com.example.demo.Services;

import com.example.demo.Dto.DatoEstadisticoDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReporteJasperService {

    // Cache de reportes ya compilados — se compilan UNA sola vez al arrancar
    private final ConcurrentHashMap<String, JasperReport> cache = new ConcurrentHashMap<>();

    /**
     * Pre-compila los reportes al arrancar la app para no gastar RAM en cada petición.
     */
    @PostConstruct
    public void preCompilarReportes() {
        preCompilar("Reports/ReporteEstadistico.jrxml");
    }

    private void preCompilar(String rutaJrxml) {
        try {
            InputStream stream = getClass().getResourceAsStream("/" + rutaJrxml);
            if (stream != null) {
                JasperReport compilado = JasperCompileManager.compileReport(stream);
                cache.put(rutaJrxml, compilado);
                System.out.println("[Jasper] Pre-compilado OK: " + rutaJrxml);
            } else {
                System.err.println("[Jasper] No se encontró: " + rutaJrxml);
            }
        } catch (Exception e) {
            System.err.println("[Jasper] Error pre-compilando " + rutaJrxml + ": " + e.getMessage());
        }
    }

    public byte[] generarReporteEstadisticoPdf(List<DatoEstadisticoDto> lista,
                                               String rutaJrxml,
                                               Map<String, Object> params) {
        try {
            // Usar el reporte ya compilado del cache
            JasperReport jasperReport = cache.get(rutaJrxml);

            if (jasperReport == null) {
                // Si por alguna razón no está en cache, compilar ahora
                InputStream stream = getClass().getResourceAsStream("/" + rutaJrxml);
                if (stream == null) {
                    throw new RuntimeException("No se encontró el reporte: " + rutaJrxml);
                }
                jasperReport = JasperCompileManager.compileReport(stream);
                cache.put(rutaJrxml, jasperReport);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Error generando el reporte Jasper: " + e.getMessage(), e);
        }
    }
}