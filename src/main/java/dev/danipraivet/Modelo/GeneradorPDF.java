package dev.danipraivet.Modelo;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase para generar documentos PDF a partir de JTable
 */
public class GeneradorPDF {

    private static final Font FONT_TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font FONT_SUBTITULO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY);
    private static final Font FONT_CABECERA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font FONT_CELDA = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

    /**
     * Genera un PDF a partir de un JTable
     * @param tabla tabla a partir de la que queramos generar el PDF
     * @param titulo titulo del PDF
     * @param rutaDestino URL destino del PDF
     * @throws DocumentException si ha habido un problema en la generación del documento
     * @throws IOException si ha habido un problema con el bufer de entrada o salida
     */
    public static void generarPDFDesdeTabla(JTable tabla, String titulo, String rutaDestino) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4.rotate()); // Horizontal para tablas anchas
        PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));

        documento.open();

        Paragraph parrafoTitulo = new Paragraph(titulo, FONT_TITULO);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        parrafoTitulo.setSpacingAfter(10);
        documento.add(parrafoTitulo);

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph parrafoFecha = new Paragraph("Generado: " + fecha, FONT_SUBTITULO);
        parrafoFecha.setAlignment(Element.ALIGN_CENTER);
        parrafoFecha.setSpacingAfter(20);
        documento.add(parrafoFecha);

        PdfPTable tablaPDF = crearTablaPDF(tabla);
        documento.add(tablaPDF);

        Paragraph piePagina = new Paragraph(
                String.format("Total de registros: %d", tabla.getRowCount()),
                FONT_SUBTITULO
        );
        piePagina.setSpacingBefore(20);
        documento.add(piePagina);

        documento.close();
    }

    /**
     * Crea una PdfPTable a partir de un JTable
     * @param tabla tabla a generar para el pdf
     * @return tabla preparada para ser usada directamente en el pdf
     */
    private static PdfPTable crearTablaPDF(JTable tabla) {
        int numColumnas = tabla.getColumnCount();
        PdfPTable tablaPDF = new PdfPTable(numColumnas);
        tablaPDF.setWidthPercentage(100);
        tablaPDF.setSpacingBefore(10f);
        tablaPDF.setSpacingAfter(10f);

        try {
            float[] anchosColumnas = new float[numColumnas];
            TableColumnModel columnModel = tabla.getColumnModel();
            for (int i = 0; i < numColumnas; i++) {
                anchosColumnas[i] = columnModel.getColumn(i).getPreferredWidth();
            }
            tablaPDF.setWidths(anchosColumnas);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        JTableHeader cabecera = tabla.getTableHeader();
        for (int i = 0; i < numColumnas; i++) {
            PdfPCell celda = new PdfPCell(new Phrase(tabla.getColumnName(i), FONT_CABECERA));
            celda.setBackgroundColor(new BaseColor(52, 73, 94)); // Azul oscuro
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(8);
            celda.setBorderWidth(1);
            tablaPDF.addCell(celda);
        }

        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            for (int columna = 0; columna < numColumnas; columna++) {
                Object valor = tabla.getValueAt(fila, columna);
                String texto = valor != null ? valor.toString() : "";

                PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_CELDA));
                celda.setPadding(5);

                if (fila % 2 == 0) {
                    celda.setBackgroundColor(BaseColor.WHITE);
                } else {
                    celda.setBackgroundColor(new BaseColor(236, 240, 241)); // Gris claro
                }

                tablaPDF.addCell(celda);
            }
        }

        return tablaPDF;
    }

    /**
     * Genera un PDF con múltiples tablas
     * Uso "varargs" en la variable tablas para facilitarle al usuario
     * ya que puede poner una o varias tablas sin tener que facilitar un
     * array de estas o una lista.
     * @param titulo titulo del PDF
     * @param rutaDestino url destino del pdf
     * @param tablas puede ser una o varias tablas
     * @throws DocumentException si ha habido un problema en la generación del documento
     * @throws IOException si ha habido un problema con el bufer de entrada o salida
     */
    public static void generarPDFConMultiplesTablas(String titulo, String rutaDestino, TablaInfo... tablas)
            throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));

        documento.open();

        Paragraph parrafoTitulo = new Paragraph(titulo, FONT_TITULO);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        parrafoTitulo.setSpacingAfter(20);
        documento.add(parrafoTitulo);

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph parrafoFecha = new Paragraph("Generado: " + fecha, FONT_SUBTITULO);
        parrafoFecha.setAlignment(Element.ALIGN_CENTER);
        parrafoFecha.setSpacingAfter(30);
        documento.add(parrafoFecha);

        for (TablaInfo info : tablas) {
            Paragraph subtitulo = new Paragraph(info.titulo, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            subtitulo.setSpacingBefore(15);
            subtitulo.setSpacingAfter(10);
            documento.add(subtitulo);

            PdfPTable tablaPDF = crearTablaPDF(info.tabla);
            documento.add(tablaPDF);

            documento.add(Chunk.NEWLINE);
        }

        documento.close();
    }

    /**
     * Abre un diálogo para seleccionar ubicación y genera el PDF
     * @param tabla tabla a generar en el pdf
     * @param tituloTabla titulo de la tabla
     * @param parent componente recipiente o padre
     */
    public static void generarPDFConDialogo(JTable tabla, String tituloTabla, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setSelectedFile(new java.io.File(tituloTabla.replaceAll("\\s+", "_") + ".pdf"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF files", "pdf"));

        int resultado = fileChooser.showSaveDialog(parent);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
                if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
                    rutaArchivo += ".pdf";
                }

                generarPDFDesdeTabla(tabla, tituloTabla, rutaArchivo);

                int respuesta = JOptionPane.showConfirmDialog(
                        parent,
                        "PDF generado correctamente en:\n" + rutaArchivo + "\n\n¿Desea abrir el archivo?",
                        "Éxito",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    abrirArchivo(rutaArchivo);
                }

            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Error al generar PDF: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }

    /**
     * Abre un archivo con la aplicación predeterminada del sistema
     * @param ruta ruta del archivo para abrir
     */
    private static void abrirArchivo(String ruta) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(ruta));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}