package com.example.demo.Services;

import com.example.demo.Dto.DatoEstadisticoDto;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.lowagie.text.pdf.draw.LineSeparator;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReporteService {

    private static final Color COLOR_HEADER  = new Color(26, 35, 126);  // azul oscuro
    private static final Color COLOR_FILA_PAR = new Color(232, 234, 246);

    public byte[] generarReporteEstadisticoPdf(List<DatoEstadisticoDto> datos, String titulo) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // ── Título ──────────────────────────────────────────────────
            Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_HEADER);
            Paragraph titulo1 = new Paragraph("Reporte Estadístico", fTitulo);
            titulo1.setAlignment(Element.ALIGN_CENTER);
            titulo1.setSpacingAfter(4);
            doc.add(titulo1);

            Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
            Paragraph sub = new Paragraph(titulo != null ? titulo : "Sistema ShenmyKappai", fSub);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(4);
            doc.add(sub);

            Font fFecha = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Paragraph pFecha = new Paragraph("Generado: " + fecha, fFecha);
            pFecha.setAlignment(Element.ALIGN_RIGHT);
            pFecha.setSpacingAfter(20);
            doc.add(pFecha);

            // ── Línea separadora ────────────────────────────────────────
            LineSeparator line = new LineSeparator(1.5f, 100, COLOR_HEADER, Element.ALIGN_CENTER, -2);
            doc.add(new Chunk(line));
            doc.add(Chunk.NEWLINE);

            // ── Tabla ───────────────────────────────────────────────────
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3f, 2f});
            tabla.setSpacingBefore(10);

            // Encabezados
            Font fHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
            agregarCeldaHeader(tabla, "Categoría", fHeader);
            agregarCeldaHeader(tabla, "Valor", fHeader);

            // Datos
            Font fData = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            int fila = 0;
            for (DatoEstadisticoDto d : datos) {
                Color bg = (fila % 2 == 0) ? Color.WHITE : COLOR_FILA_PAR;
                agregarCeldaDato(tabla, d.getCategoria() != null ? d.getCategoria() : "-", fData, bg, Element.ALIGN_LEFT);
                agregarCeldaDato(tabla, String.format("%,.2f", d.getValor()), fData, bg, Element.ALIGN_RIGHT);
                fila++;
            }

            doc.add(tabla);

            // ── Total ────────────────────────────────────────────────────
            double total = datos.stream().mapToDouble(DatoEstadisticoDto::getValor).sum();
            Font fTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, COLOR_HEADER);
            Paragraph pTotal = new Paragraph("Total: " + String.format("%,.2f", total), fTotal);
            pTotal.setAlignment(Element.ALIGN_RIGHT);
            pTotal.setSpacingBefore(8);
            doc.add(pTotal);

            // ── Pie de página ────────────────────────────────────────────
            Font fPie = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
            Paragraph pie = new Paragraph("© ShenmyKappai - Reporte generado automáticamente", fPie);
            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(30);
            doc.add(pie);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private void agregarCeldaHeader(PdfPTable tabla, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(COLOR_HEADER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        cell.setBorderColor(Color.WHITE);
        tabla.addCell(cell);
    }

    private void agregarCeldaDato(PdfPTable tabla, String texto, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setPadding(6);
        cell.setBorderColor(new Color(200, 200, 200));
        tabla.addCell(cell);
    }
}