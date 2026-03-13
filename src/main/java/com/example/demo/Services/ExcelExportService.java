package com.example.demo.Services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.example.demo.Model.Usuario;

@Service
public class ExcelExportService {

    public void exportarUsuariosAExcel(List<Usuario> usuarios, HttpServletResponse response) throws IOException {

        // Headless explícito para Railway/Linux (evita errores de AWT en autoSizeColumn)
        System.setProperty("java.awt.headless", "true");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        // ===== ESTILOS =====

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setWrapText(false);

        CellStyle adminDataStyle = workbook.createCellStyle();
        adminDataStyle.cloneStyleFrom(dataStyle);
        adminDataStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        adminDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ===== ENCABEZADOS =====

        String[] headers = {"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Tipo", "Estado", "Fecha Creación"};
        // Anchos fijos en unidades POI (1 unidad ≈ 1/256 de carácter) — sin autoSizeColumn
        int[] colWidths = {2000, 5000, 5000, 4000, 7000, 4500, 3500, 5500};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, colWidths[i]);
        }

        // ===== DATOS =====

        int rowNum = 1;
        for (Usuario usuario : usuarios) {
            Row row = sheet.createRow(rowNum++);
            boolean esAdmin = (usuario.getRol() != null && usuario.getRol().getIdRol() == 1);
            CellStyle style = esAdmin ? adminDataStyle : dataStyle;

            createCell(row, 0, usuario.getIdUsuario() != null ? String.valueOf(usuario.getIdUsuario()) : "", style);
            createCell(row, 1, usuario.getNombreUsuario(), style);
            createCell(row, 2, usuario.getApellidoUsuario(), style);
            createCell(row, 3, usuario.getTelUsuario(), style);
            createCell(row, 4, usuario.getCorreoUsuario(), style);
            createCell(row, 5, esAdmin ? "Administrador" : "Usuario", style);
            createCell(row, 6, usuario.getEstadoUsuario(), style);
            createCell(row, 7, usuario.getFechaCreacion() != null ? usuario.getFechaCreacion().toString() : "", style);
        }

        // ===== ESCRIBIR RESPONSE =====
        try {
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } finally {
            workbook.close();
        }
    }

    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}