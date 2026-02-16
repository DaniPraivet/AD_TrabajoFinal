package dev.danipraivet.Vista;

import dev.danipraivet.ControladorBBDD.Controlador;
import dev.danipraivet.Modelo.Arma;
import dev.danipraivet.Modelo.Leyenda;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana emergente para agregar una nueva leyenda
 */
public class VentanaAgregarLeyenda extends JFrame {

    private VentanaPrincipal ventanaPrincipal;
    private Controlador controlador;

    private JTextField txtNombre;
    private JSpinner spVida, spFuerza, spVelocidad, spDestreza, spDefensa;
    private JComboBox<Arma> cmbArma1, cmbArma2;
    private JButton btnGuardar, btnCancelar;

    /**
     * Constructor principal
     * @param ventanaPrincipal ventana padre
     * @param controlador controlador de la base de datos
     */
    public VentanaAgregarLeyenda(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;

        setTitle("Agregar Leyenda");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);

        initComponentes();
        cargarArmas();
    }

    private void initComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Nombre
        agregarFila(panel, gbc, 0, "Nombre:", txtNombre = new JTextField(20));

        // Stats con JSpinner
        spVida      = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spFuerza    = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spVelocidad = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spDestreza  = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spDefensa   = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));

        agregarFila(panel, gbc, 1, "Vida:",      spVida);
        agregarFila(panel, gbc, 2, "Fuerza:",    spFuerza);
        agregarFila(panel, gbc, 3, "Velocidad:", spVelocidad);
        agregarFila(panel, gbc, 4, "Destreza:",  spDestreza);
        agregarFila(panel, gbc, 5, "Defensa:",   spDefensa);

        // ComboBox de armas
        cmbArma1 = new JComboBox<>();
        cmbArma2 = new JComboBox<>();
        agregarFila(panel, gbc, 6, "Arma 1:", cmbArma1);
        agregarFila(panel, gbc, 7, "Arma 2:", cmbArma2);

        // Botones
        JPanel panelBotones = new JPanel();
        btnGuardar  = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarLeyenda());
        btnCancelar.addActionListener(e -> dispose());
    }

    /** Utilidad para agregar una fila etiqueta + componente al GridBag */
    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    private void cargarArmas() {
        List<Arma> armas = controlador.obtenerArmas();
        for (Arma a : armas) {
            cmbArma1.addItem(a);
            cmbArma2.addItem(a);
        }
        // Preseleccionar armas distintas si hay al menos 2
        if (cmbArma2.getItemCount() > 1) {
            cmbArma2.setSelectedIndex(1);
        }
    }

    private void guardarLeyenda() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Arma arma1 = (Arma) cmbArma1.getSelectedItem();
        Arma arma2 = (Arma) cmbArma2.getSelectedItem();

        if (arma1 == null || arma2 == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar las dos armas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (arma1.getId() == arma2.getId()) {
            JOptionPane.showMessageDialog(this, "Las dos armas deben ser diferentes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Leyenda leyenda = new Leyenda(
                0,
                nombre,
                (int) spVida.getValue(),
                (int) spFuerza.getValue(),
                (int) spVelocidad.getValue(),
                (int) spDestreza.getValue(),
                (int) spDefensa.getValue(),
                arma1,
                arma2
        );

        if (controlador.agregarLeyenda(leyenda)) {
            JOptionPane.showMessageDialog(this, "Leyenda guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarDatosLeyendas();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la leyenda.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
