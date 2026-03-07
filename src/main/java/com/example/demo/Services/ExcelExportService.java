package com.example.demo.Services;

// Imports para Apache POI
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Imports para Spring y Servlet
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;

// Imports para Java básico
import java.io.IOException;
import java.util.List;

// Import de tu modelo
import com.example.demo.Model.Usuario;

@Service
public class ExcelExportService {

    public void exportarUsuariosAExcel(List<Usuario> usuarios, HttpServletResponse response) throws IOException {

        // Crear libro de Excel (.xlsx)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        // ===== CREAR ESTILOS =====

        // Estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Estilo para datos
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setWrapText(true);

        // Estilo para datos de administradores (fondo diferente)
        CellStyle adminDataStyle = workbook.createCellStyle();
        adminDataStyle.cloneStyleFrom(dataStyle);
        adminDataStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        adminDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ===== CREAR ENCABEZADOS =====

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Tipo", "Estado", "Fecha Creación"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== LLENAR DATOS =====

        int rowNum = 1;
        for (Usuario usuario : usuarios) {
            Row row = sheet.createRow(rowNum++);

            // Determinar si es admin para aplicar estilo diferente
            boolean esAdmin = (usuario.getRol() != null && usuario.getRol().getIdRol() == 1);
            CellStyle styleToUse = esAdmin ? adminDataStyle : dataStyle;

            // ID
            Cell cellId = row.createCell(0);
            if (usuario.getIdUsuario() != null) {
                cellId.setCellValue(usuario.getIdUsuario());
            }
            cellId.setCellStyle(styleToUse);

            // Nombre
            Cell cellNombre = row.createCell(1);
            cellNombre.setCellValue(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "");
            cellNombre.setCellStyle(styleToUse);

            // Apellido
            Cell cellApellido = row.createCell(2);
            cellApellido.setCellValue(usuario.getApellidoUsuario() != null ? usuario.getApellidoUsuario() : "");
            cellApellido.setCellStyle(styleToUse);

            // Teléfono
            Cell cellTelefono = row.createCell(3);
            cellTelefono.setCellValue(usuario.getTelUsuario() != null ? usuario.getTelUsuario() : "");
            cellTelefono.setCellStyle(styleToUse);

            // Correo
            Cell cellCorreo = row.createCell(4);
            cellCorreo.setCellValue(usuario.getCorreoUsuario() != null ? usuario.getCorreoUsuario() : "");
            cellCorreo.setCellStyle(styleToUse);

            // Tipo (según rol)
            Cell cellTipo = row.createCell(5);
            String tipoUsuario = "Usuario";
            if (usuario.getRol() != null) {
                tipoUsuario = usuario.getRol().getIdRol() == 1 ? "Administrador" : "Usuario";
            }
            cellTipo.setCellValue(tipoUsuario);
            cellTipo.setCellStyle(styleToUse);

            // Estado
            Cell cellEstado = row.createCell(6);
            cellEstado.setCellValue(usuario.getEstadoUsuario() != null ? usuario.getEstadoUsuario() : "");
            cellEstado.setCellStyle(styleToUse);

            // Fecha de creación
            Cell cellFecha = row.createCell(7);
            if (usuario.getFechaCreacion() != null) {
                cellFecha.setCellValue(usuario.getFechaCreacion().toString());
            }
            cellFecha.setCellStyle(styleToUse);
        }

        // ===== AJUSTAR TAMAÑOS DE COLUMNA =====

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Asegurar un ancho mínimo
            if (sheet.getColumnWidth(i) < 3000) {
                sheet.setColumnWidth(i, 3000);
            }
            // Limitar ancho máximo
            if (sheet.getColumnWidth(i) > 8000) {
                sheet.setColumnWidth(i, 8000);
            }
        }

        // ===== ESCRIBIR AL RESPONSE Y CERRAR =====

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}