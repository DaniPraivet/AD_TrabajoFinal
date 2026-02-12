package dev.danipraivet.Modelo;

import instituto.Vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

/**
 * Clase gestora que maneja las operaciones de PDF
 * Integra el VisorPDF y el GeneradorPDF
 */
public class GestorPDF {

    private VisorPDF visorPDF;
    private VentanaPrincipal componentePadre;

    public GestorPDF(VisorPDF visorPDF, VentanaPrincipal componentePadre) {
        this.visorPDF = visorPDF;
        this.componentePadre = componentePadre;
    }

    /**
     * Abre un diálogo para seleccionar y cargar un PDF
     */
    public void cargarPDFConDialogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int resultado = fileChooser.showOpenDialog(componentePadre);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            cargarPDF(archivo);
        }
    }

    /**
     * Carga un archivo PDF en el visor
     */
    public void cargarPDF(File archivo) {
        if (archivo == null || !archivo.exists()) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "El archivo no existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "El archivo seleccionado no es un PDF",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            visorPDF.cargarPDF(archivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "Error al cargar el PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Carga un PDF desde una ruta
     */
    public void cargarPDF(String ruta) {
        cargarPDF(new File(ruta));
    }

    /**
     * Genera un PDF desde una tabla con diálogo de guardado
     */
    public void generarPDFDesdeTabla(JTable tabla, String tituloTabla) {
        if (tabla == null || tabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "La tabla está vacía",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        GeneradorPDF.generarPDFConDialogo(tabla, tituloTabla, componentePadre);
    }

    /**
     * Genera un PDF con múltiples tablas
     */
    public void generarPDFMultiplesTablas(String titulo, TablaInfo... tablas) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setSelectedFile(new File(titulo.replaceAll("\\s+", "_") + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));

        int resultado = fileChooser.showSaveDialog(componentePadre);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
                if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
                    rutaArchivo += ".pdf";
                }

                GeneradorPDF.generarPDFConMultiplesTablas(titulo, rutaArchivo, tablas);

                int respuesta = JOptionPane.showConfirmDialog(
                        componentePadre,
                        "PDF generado correctamente en:\n" + rutaArchivo + "\n\n¿Desea abrir el archivo?",
                        "Éxito",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    abrirArchivo(rutaArchivo);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        componentePadre,
                        "Error al generar PDF: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }

    /**
     * Genera PDF de la tabla seleccionada según el tab activo
     */
    public void generarPDFTablaActual(JTabbedPane tabbedPane, JTable[] tablas, String[] nombresTablas) {
        int indiceActivo = tabbedPane.getSelectedIndex();
        if (indiceActivo >= 0 && indiceActivo < tablas.length) {
            generarPDFDesdeTabla(tablas[indiceActivo], nombresTablas[indiceActivo]);
        }
    }

    /**
     * Cierra el documento actual del visor
     */
    public void cerrarDocumento() {
        visorPDF.cerrarDocumento();
    }

    /**
     * Abre un archivo con la aplicación predeterminada
     */
    private void abrirArchivo(String ruta) {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(new File(ruta));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VisorPDF getVisorPDF() {
        return visorPDF;
    }
}