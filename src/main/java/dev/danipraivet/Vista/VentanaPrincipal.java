package dev.danipraivet.Vista;

import dev.danipraivet.ControladorBBDD.Controlador;
import dev.danipraivet.Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal del sistema Brawlhalla
 */
public class VentanaPrincipal extends JFrame {

    /** Controlador de acceso a la base de datos */
    private Controlador controlador;

    private JTable tablaLeyendas;
    private JTable tablaArmas;

    private JTabbedPane menuInterior;
    private JSplitPane panelDivisorCentral;
    private JPanel panelPrincipal;
    private JMenuBar menuBar;
    private JMenu menuLeyenda, menuArma;
    private JMenuItem menuItemAgregarLeyenda, menuItemEliminarLeyenda;
    private JMenuItem menuItemAgregarArma, menuItemEliminarArma;

    public VentanaPrincipal() {
        EstiloAplicacion.aplicarEstilo();
        controlador = new Controlador();

        setTitle("Brawlhalla - Gestor de Leyendas");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initGUI();

        cargarDatosLeyendas();
        cargarDatosArmas();
    }

    public void initGUI() {
        JPanel panelTablaLeyendas = crearPanelLeyendas();
        JPanel panelTablaArmas    = crearPanelArmas();

        menuInterior = new JTabbedPane();
        menuInterior.addTab("Leyendas", panelTablaLeyendas);
        menuInterior.addTab("Armas",    panelTablaArmas);

        panelDivisorCentral = new JSplitPane();
        panelDivisorCentral.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        panelDivisorCentral.setDividerSize(5);
        panelDivisorCentral.setDividerLocation(200);
        panelDivisorCentral.setResizeWeight(0.0);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(Color.DARK_GRAY);
        JLabel lblLogo = new JLabel("BRAWLHALLA", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
        lblLogo.setForeground(Color.WHITE);
        panelIzquierdo.add(lblLogo, BorderLayout.CENTER);

        panelDivisorCentral.setLeftComponent(panelIzquierdo);
        panelDivisorCentral.setRightComponent(menuInterior);

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelDivisorCentral, BorderLayout.CENTER);

        crearMenu();
        add(panelPrincipal);
        setVisible(true);
    }

    private void crearMenu() {
        menuBar = new JMenuBar();

        // Menú Leyendas
        menuLeyenda = new JMenu("Leyendas");
        menuItemAgregarLeyenda  = new JMenuItem("Añadir leyenda");
        menuItemEliminarLeyenda = new JMenuItem("Eliminar leyenda");
        menuItemAgregarLeyenda.addActionListener(e -> abrirVentanaAgregarLeyenda());
        menuItemEliminarLeyenda.addActionListener(e -> eliminarLeyendaSeleccionada());
        menuLeyenda.add(menuItemAgregarLeyenda);
        menuLeyenda.add(menuItemEliminarLeyenda);

        // Menú Armas
        menuArma = new JMenu("Armas");
        menuItemAgregarArma  = new JMenuItem("Añadir arma");
        menuItemEliminarArma = new JMenuItem("Eliminar arma");
        menuItemAgregarArma.addActionListener(e -> abrirVentanaAgregarArma());
        menuItemEliminarArma.addActionListener(e -> eliminarArmaSeleccionada());
        menuArma.add(menuItemAgregarArma);
        menuArma.add(menuItemEliminarArma);

        menuBar.add(menuLeyenda);
        menuBar.add(menuArma);
        setJMenuBar(menuBar);
    }

    // ─────────────────────────────────────────────
    //  PANELES DE TABLA
    // ─────────────────────────────────────────────

    private JPanel crearPanelLeyendas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Vida", "Fuerza", "Velocidad", "Destreza", "Defensa", "Arma 1", "Arma 2" };
        tablaLeyendas = new JTable(new DefaultTableModel(columnas, 0));
        RenderizadorCabecera.aplicarEstiloCabeceras(tablaLeyendas);
        // Ocultar columna ID (índice 0) visualmente pero mantenerla para operaciones
        tablaLeyendas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaLeyendas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaLeyendas.getColumnModel().getColumn(0).setWidth(0);
        panel.add(new JScrollPane(tablaLeyendas), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelArmas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "ID", "Nombre Arma" };
        tablaArmas = new JTable(new DefaultTableModel(columnas, 0));
        RenderizadorCabecera.aplicarEstiloCabeceras(tablaArmas);
        tablaArmas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaArmas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaArmas.getColumnModel().getColumn(0).setWidth(0);
        panel.add(new JScrollPane(tablaArmas), BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────
    //  CARGA DE DATOS
    // ─────────────────────────────────────────────

    public void cargarDatosLeyendas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaLeyendas.getModel();
        modelo.setRowCount(0);
        List<Leyenda> leyendas = controlador.obtenerLeyendas();
        for (Leyenda l : leyendas) {
            modelo.addRow(new Object[]{
                    l.getId(),
                    l.getNombre(),
                    l.getVida(),
                    l.getFuerza(),
                    l.getVelocidad(),
                    l.getDestreza(),
                    l.getDefensa(),
                    l.getArma1() != null ? l.getArma1().getNombreArma() : "",
                    l.getArma2() != null ? l.getArma2().getNombreArma() : ""
            });
        }
    }

    public void cargarDatosArmas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaArmas.getModel();
        modelo.setRowCount(0);
        List<Arma> armas = controlador.obtenerArmas();
        for (Arma a : armas) {
            modelo.addRow(new Object[]{ a.getId(), a.getNombreArma() });
        }
    }

    // ─────────────────────────────────────────────
    //  ACCIONES
    // ─────────────────────────────────────────────

    private void abrirVentanaAgregarLeyenda() {
        new VentanaAgregarLeyenda(this, controlador).setVisible(true);
    }

    private void abrirVentanaAgregarArma() {
        new VentanaAgregarArma(this, controlador).setVisible(true);
    }

    private void eliminarLeyendaSeleccionada() {
        int fila = tablaLeyendas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una leyenda para eliminar");
            return;
        }
        int id = (int) tablaLeyendas.getModel().getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar esta leyenda?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.eliminarLeyenda(id)) {
                cargarDatosLeyendas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la leyenda");
            }
        }
    }

    private void eliminarArmaSeleccionada() {
        int fila = tablaArmas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un arma para eliminar");
            return;
        }
        int id = (int) tablaArmas.getModel().getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este arma?\n(Fallará si alguna leyenda la usa)", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.eliminarArma(id)) {
                cargarDatosArmas();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el arma. Comprueba que ninguna leyenda la esté usando.");
            }
        }
    }
}
