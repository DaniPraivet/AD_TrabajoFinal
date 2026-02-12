package dev.danipraivet.Modelo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente para visualizar archivos PDF en un JPanel
 */
public class VisorPDF extends JPanel {
    /**
     * Documento pdf donde se va a trabajar
     */
    private PDDocument documento;
    /**
     * Render a usar para poder visualizar el pdf
     */
    private PDFRenderer renderizador;
    /**
     * Lista de todas las páginas del documento
     */
    private List<BufferedImage> paginas;
    /**
     * Numero de la página actual donde estamos
     */
    private int paginaActual;
    /**
     * Donde vamos a mostrar el contenido del pdf
     */
    private JPanel panelContenido;
    /**
     * Componente para poder "desplazarnos" vertical u horizontalmente
     */
    private JScrollPane scrollPane;
    /**
     * Indicador de la página en la que nos situamos
     */
    private JLabel lblPagina;
    /**
     * Botones para avanzar o retroceder páginas
     */
    private JButton btnAnterior, btnSiguiente;
    /**
     * Panel de control para los diferentes botones adicionales
     */
    private JPanel panelControles;
    /**
     * Zoom que aplicaremos para mostrar el PDF
     */
    private double escala;

    /**
     * Constuctor principal
     */
    public VisorPDF() {
        paginas = new ArrayList<>();
        paginaActual = 0;
        escala = 1.0;
        initComponents();
    }

    /**
     * Iniciamos los componentes
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de contenido para mostrar la página
        panelContenido = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (paginas != null && !paginas.isEmpty() && paginaActual < paginas.size()) {
                    BufferedImage imagen = paginas.get(paginaActual);
                    if (imagen != null) {
                        int w = (int) (imagen.getWidth() * escala);
                        int h = (int) (imagen.getHeight() * escala);

                        // Centrar la imagen
                        int x = (getWidth() - w) / 2;
                        int y = 10;

                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.drawImage(imagen, x, y, w, h, null);
                    }
                }
            }
        };
        panelContenido.setBackground(Color.GRAY);
        panelContenido.setPreferredSize(new Dimension(600, 800));

        scrollPane = new JScrollPane(panelContenido);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de controles
        panelControles = new JPanel(new FlowLayout());
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        lblPagina = new JLabel("Sin documento");

        JButton btnZoomIn = new JButton("+");
        JButton btnZoomOut = new JButton("-");
        JButton btnZoomReset = new JButton("100%");

        btnAnterior.addActionListener(e -> paginaAnterior());
        btnSiguiente.addActionListener(e -> paginaSiguiente());
        btnZoomIn.addActionListener(e -> ajustarZoom(0.1));
        btnZoomOut.addActionListener(e -> ajustarZoom(-0.1));
        btnZoomReset.addActionListener(e -> resetearZoom());

        panelControles.add(btnAnterior);
        panelControles.add(lblPagina);
        panelControles.add(btnSiguiente);
        panelControles.add(new JSeparator(SwingConstants.VERTICAL));
        panelControles.add(btnZoomOut);
        panelControles.add(btnZoomReset);
        panelControles.add(btnZoomIn);

        add(panelControles, BorderLayout.SOUTH);

        actualizarEstadoBotones();
    }

    /**
     * Carga un archivo PDF para visualización
     */
    public void cargarPDF(File archivo) throws IOException {
        cerrarDocumento();

        documento = PDDocument.load(archivo);
        renderizador = new PDFRenderer(documento);
        paginas.clear();

        for (int i = 0; i < documento.getNumberOfPages(); i++) {
            BufferedImage imagen = renderizador.renderImageWithDPI(i, 150);
            paginas.add(imagen);
        }

        paginaActual = 0;
        escala = 1.0;
        actualizarVisualizacion();

        JOptionPane.showMessageDialog(this,
                "PDF cargado: " + documento.getNumberOfPages() + " páginas",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Carga un PDF desde una ruta
     */
    public void cargarPDF(String ruta) throws IOException {
        cargarPDF(new File(ruta));
    }

    private void paginaAnterior() {
        if (paginaActual > 0) {
            paginaActual--;
            actualizarVisualizacion();
        }
    }

    private void paginaSiguiente() {
        if (paginaActual < paginas.size() - 1) {
            paginaActual++;
            actualizarVisualizacion();
        }
    }

    private void ajustarZoom(double delta) {
        escala += delta;
        if (escala < 0.25) escala = 0.25;
        if (escala > 3.0) escala = 3.0;
        actualizarVisualizacion();
    }

    private void resetearZoom() {
        escala = 1.0;
        actualizarVisualizacion();
    }

    private void actualizarVisualizacion() {
        if (!paginas.isEmpty()) {
            lblPagina.setText(String.format("Página %d de %d (%.0f%%)",
                    paginaActual + 1,
                    paginas.size(),
                    escala * 100));

            BufferedImage imagen = paginas.get(paginaActual);
            int w = (int) (imagen.getWidth() * escala);
            int h = (int) (imagen.getHeight() * escala);
            panelContenido.setPreferredSize(new Dimension(w + 20, h + 20));

            panelContenido.revalidate();
            panelContenido.repaint();
        }
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        btnAnterior.setEnabled(paginaActual > 0);
        btnSiguiente.setEnabled(!paginas.isEmpty() && paginaActual < paginas.size() - 1);
    }

    /**
     * Cierra el documento actual
     */
    public void cerrarDocumento() {
        if (documento != null) {
            try {
                documento.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        paginas.clear();
        paginaActual = 0;
        lblPagina.setText("Sin documento");
        panelContenido.repaint();
        actualizarEstadoBotones();
    }

    /**
     * Obtiene el número total de páginas
     */
    public int getTotalPaginas() {
        return paginas.size();
    }

    /**
     * Obtiene la página actual
     */
    public int getPaginaActual() {
        return paginaActual;
    }
}