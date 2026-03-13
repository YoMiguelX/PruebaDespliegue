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
                                               String rutaJrxml,
                                               Map<String, Object> params) {
        try {
            // Cargar el .jasper pre-compilado directamente (no compilar .jrxml en runtime)
            String rutaJasper = rutaJrxml.replace(".jrxml", ".jasper");
            InputStream jasperStream = getClass().getResourceAsStream("/" + rutaJasper);

            if (jasperStream == null) {
                throw new RuntimeException("No se encontró el archivo: " + rutaJasper
                        + " — asegúrate de que esté en src/main/resources/" + rutaJasper);
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Error generando reporte: " + e.getMessage(), e);
        }
    }
}