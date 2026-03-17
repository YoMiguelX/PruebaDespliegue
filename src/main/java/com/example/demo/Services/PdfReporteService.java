package com.example.demo.Services;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfReporteService {

    private final UsuarioRepository usuarioRepository;
    private final JugadorRepository jugadorRepository;
    private final ProgresoJugadorRepository progresoRepository;

    // Colores
    private static final Color AZUL_OSCURO  = new Color(26, 35, 126);
    private static final Color AZUL_CLARO   = new Color(197, 202, 233);
    private static final Color VERDE        = new Color(27, 94, 32);
    private static final Color VERDE_CLARO  = new Color(200, 230, 201);
    private static final Color GRIS_CLARO   = new Color(245, 245, 245);
    private static final Color ROJO         = new Color(183, 28, 28);

    public PdfReporteService(UsuarioRepository usuarioRepository,
                             JugadorRepository jugadorRepository,
                             ProgresoJugadorRepository progresoRepository) {
        this.usuarioRepository  = usuarioRepository;
        this.jugadorRepository  = jugadorRepository;
        this.progresoRepository = progresoRepository;
    }

    public byte[] generarReporteCompleto() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document doc = new Document(PageSize.A4, 40, 40, 50, 40);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // ── Datos de la BD ───────────────────────────────────────────
            List<Usuario> todosUsuarios = usuarioRepository.findAll();
            List<Jugador> todosJugadores = jugadorRepository.findAll();
            List<ProgresoJugador> todosProgresos = progresoRepository.findAll();

            long usuariosActivos   = todosUsuarios.stream().filter(u -> "Activo".equalsIgnoreCase(u.getEstadoUsuario())).count();
            long usuariosInactivos = todosUsuarios.stream().filter(u -> "Inactivo".equalsIgnoreCase(u.getEstadoUsuario())).count();
            long totalAdmins       = todosUsuarios.stream().filter(u -> u.getRol() != null && u.getRol().getIdRol() == 1).count();
            long totalJugadoresActivos = todosJugadores.stream().filter(j -> "Activo".equalsIgnoreCase(j.getEstado())).count();
            long nivelesCompletados    = todosProgresos.stream().filter(p -> Boolean.TRUE.equals(p.getNivelCompletado())).count();

            // ── PORTADA ──────────────────────────────────────────────────
            agregarPortada(doc, todosUsuarios.size());

            // ── SECCIÓN 1: Resumen general ───────────────────────────────
            agregarSeccion(doc, "1. Resumen General del Sistema");

            PdfPTable resumen = new PdfPTable(2);
            resumen.setWidthPercentage(100);
            resumen.setWidths(new float[]{3f, 2f});
            resumen.setSpacingBefore(8);

            agregarFilaResumen(resumen, "Total de usuarios registrados",  String.valueOf(todosUsuarios.size()), GRIS_CLARO);
            agregarFilaResumen(resumen, "Usuarios activos",               String.valueOf(usuariosActivos),      VERDE_CLARO);
            agregarFilaResumen(resumen, "Usuarios inactivos",             String.valueOf(usuariosInactivos),    new Color(255, 235, 238));
            agregarFilaResumen(resumen, "Administradores",                String.valueOf(totalAdmins),          AZUL_CLARO);
            agregarFilaResumen(resumen, "Total jugadores",                String.valueOf(todosJugadores.size()), GRIS_CLARO);
            agregarFilaResumen(resumen, "Jugadores activos",              String.valueOf(totalJugadoresActivos), VERDE_CLARO);
            agregarFilaResumen(resumen, "Niveles completados (total)",    String.valueOf(nivelesCompletados),   AZUL_CLARO);
            doc.add(resumen);

            // ── SECCIÓN 2: Top 10 jugadores por puntaje ──────────────────
            agregarEspacio(doc);
            agregarSeccion(doc, "2. Top 10 Jugadores por Puntaje");

            List<ProgresoJugador> topPorPuntaje = todosProgresos.stream()
                    .filter(p -> p.getPuntajeNivel() != null && p.getJugador() != null)
                    .sorted(Comparator.comparingInt(ProgresoJugador::getPuntajeNivel).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            if (topPorPuntaje.isEmpty()) {
                doc.add(sinDatos());
            } else {
                PdfPTable tablaTop = new PdfPTable(4);
                tablaTop.setWidthPercentage(100);
                tablaTop.setWidths(new float[]{0.7f, 2.5f, 2f, 1.5f});
                tablaTop.setSpacingBefore(8);

                agregarEncabezadoTabla(tablaTop, new String[]{"#", "Jugador", "Nivel", "Puntaje"});

                int pos = 1;
                for (ProgresoJugador p : topPorPuntaje) {
                    Color bg = (pos % 2 == 0) ? GRIS_CLARO : Color.WHITE;
                    String nombreJugador = p.getJugador().getNombre() != null ? p.getJugador().getNombre() : "-";
                    String nombreNivel   = (p.getNivel() != null && p.getNivel().getNombreNivel() != null)
                            ? p.getNivel().getNombreNivel() : "-";
                    agregarFilaDato(tablaTop, new String[]{
                            String.valueOf(pos),
                            nombreJugador,
                            nombreNivel,
                            String.valueOf(p.getPuntajeNivel())
                    }, bg);
                    pos++;
                }
                doc.add(tablaTop);
            }

            // ── SECCIÓN 3: Progreso por nivel ─────────────────────────────
            agregarEspacio(doc);
            agregarSeccion(doc, "3. Estadísticas por Nivel");

            Map<String, List<ProgresoJugador>> porNivel = todosProgresos.stream()
                    .filter(p -> p.getNivel() != null)
                    .collect(Collectors.groupingBy(p -> p.getNivel().getNombreNivel() != null
                            ? p.getNivel().getNombreNivel() : "Sin nombre"));

            if (porNivel.isEmpty()) {
                doc.add(sinDatos());
            } else {
                PdfPTable tablaNivel = new PdfPTable(5);
                tablaNivel.setWidthPercentage(100);
                tablaNivel.setWidths(new float[]{2.5f, 1.5f, 1.5f, 1.5f, 1.5f});
                tablaNivel.setSpacingBefore(8);

                agregarEncabezadoTabla(tablaNivel,
                        new String[]{"Nivel", "Jugadores", "Completados", "Puntaje Promedio", "Tiempo Prom (min)"});

                int fila = 0;
                for (Map.Entry<String, List<ProgresoJugador>> entry : porNivel.entrySet()) {
                    Color bg = (fila % 2 == 0) ? GRIS_CLARO : Color.WHITE;
                    List<ProgresoJugador> progs = entry.getValue();
                    long completados = progs.stream().filter(p -> Boolean.TRUE.equals(p.getNivelCompletado())).count();
                    double promPuntaje = progs.stream()
                            .filter(p -> p.getPuntajeNivel() != null)
                            .mapToInt(ProgresoJugador::getPuntajeNivel).average().orElse(0);
                    double promTiempo = progs.stream()
                            .filter(p -> p.getTiempoJugado() != null)
                            .mapToInt(ProgresoJugador::getTiempoJugado).average().orElse(0);

                    agregarFilaDato(tablaNivel, new String[]{
                            entry.getKey(),
                            String.valueOf(progs.size()),
                            completados + " (" + (progs.size() > 0 ? (completados * 100 / progs.size()) : 0) + "%)",
                            String.format("%.1f", promPuntaje),
                            String.format("%.1f", promTiempo / 60.0)
                    }, bg);
                    fila++;
                }
                doc.add(tablaNivel);
            }

            // ── SECCIÓN 4: Usuarios recientes ─────────────────────────────
            agregarEspacio(doc);
            agregarSeccion(doc, "4. Últimos 10 Usuarios Registrados");

            List<Usuario> recientes = todosUsuarios.stream()
                    .filter(u -> u.getFechaCreacion() != null)
                    .sorted(Comparator.comparing(Usuario::getFechaCreacion).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            if (recientes.isEmpty()) {
                doc.add(sinDatos());
            } else {
                PdfPTable tablaRec = new PdfPTable(4);
                tablaRec.setWidthPercentage(100);
                tablaRec.setWidths(new float[]{2.5f, 2.5f, 2f, 1.5f});
                tablaRec.setSpacingBefore(8);

                agregarEncabezadoTabla(tablaRec, new String[]{"Nombre", "Correo", "Fecha Registro", "Estado"});

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                int fila = 0;
                for (Usuario u : recientes) {
                    Color bg = (fila % 2 == 0) ? GRIS_CLARO : Color.WHITE;
                    agregarFilaDato(tablaRec, new String[]{
                            (u.getNombreUsuario() != null ? u.getNombreUsuario() : "") + " "
                                    + (u.getApellidoUsuario() != null ? u.getApellidoUsuario() : ""),
                            u.getCorreoUsuario() != null ? u.getCorreoUsuario() : "-",
                            u.getFechaCreacion().format(fmt),
                            u.getEstadoUsuario() != null ? u.getEstadoUsuario() : "-"
                    }, bg);
                    fila++;
                }
                doc.add(tablaRec);
            }

            // ── PIE ───────────────────────────────────────────────────────
            agregarEspacio(doc);
            doc.add(new Chunk(new LineSeparator(0.5f, 100, Color.GRAY, Element.ALIGN_CENTER, -2)));
            Font fPie = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
            Paragraph pie = new Paragraph("© ShenmiKappai — Reporte generado automáticamente", fPie);
            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(5);
            doc.add(pie);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void agregarPortada(Document doc, int totalUsuarios) throws DocumentException {
        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, AZUL_OSCURO);
        Paragraph titulo = new Paragraph("Reporte Estadístico del Sistema", fTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(4);
        doc.add(titulo);

        Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
        Paragraph sub = new Paragraph("ShenmiKappai — Juego Educativo", fSub);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(4);
        doc.add(sub);

        Font fFecha = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
        String fecha = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Paragraph pFecha = new Paragraph("Generado: " + fecha, fFecha);
        pFecha.setAlignment(Element.ALIGN_CENTER);
        pFecha.setSpacingAfter(15);
        doc.add(pFecha);

        doc.add(new Chunk(new LineSeparator(1.5f, 100, AZUL_OSCURO, Element.ALIGN_CENTER, -2)));
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccion(Document doc, String titulo) throws DocumentException {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, AZUL_OSCURO);
        Paragraph p = new Paragraph(titulo, f);
        p.setSpacingBefore(10);
        p.setSpacingAfter(2);
        doc.add(p);
    }

    private void agregarEspacio(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
    }

    private Paragraph sinDatos() {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);
        return new Paragraph("Sin datos disponibles.", f);
    }

    private void agregarEncabezadoTabla(PdfPTable tabla, String[] columnas) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, f));
            cell.setBackgroundColor(AZUL_OSCURO);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7);
            cell.setBorderColor(Color.WHITE);
            tabla.addCell(cell);
        }
    }

    private void agregarFilaDato(PdfPTable tabla, String[] valores, Color bg) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        for (String val : valores) {
            PdfPCell cell = new PdfPCell(new Phrase(val != null ? val : "-", f));
            cell.setBackgroundColor(bg);
            cell.setPadding(5);
            cell.setBorderColor(new Color(210, 210, 210));
            tabla.addCell(cell);
        }
    }

    private void agregarFilaResumen(PdfPTable tabla, String etiqueta, String valor, Color bg) {
        Font fLabel = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font fValor = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, AZUL_OSCURO);

        PdfPCell cLabel = new PdfPCell(new Phrase(etiqueta, fLabel));
        cLabel.setBackgroundColor(bg);
        cLabel.setPadding(7);
        cLabel.setBorderColor(new Color(210, 210, 210));
        tabla.addCell(cLabel);

        PdfPCell cValor = new PdfPCell(new Phrase(valor, fValor));
        cValor.setBackgroundColor(bg);
        cValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        cValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cValor.setPadding(7);
        cValor.setBorderColor(new Color(210, 210, 210));
        tabla.addCell(cValor);
    }
}