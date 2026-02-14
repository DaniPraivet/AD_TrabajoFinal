package dev.danipraivet.Vista;

import dev.danipraivet.ControladorBBDD.Controlador;
import dev.danipraivet.Modelo.Arma;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana emergente para agregar un nuevo tipo de arma
 */
public class VentanaAgregarArma extends JFrame {

    private JTextField txtNombreArma;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private VentanaPrincipal ventanaPrincipal;
    private Controlador controlador;

    /**
     * Constructor principal
     * @param ventanaPrincipal ventana padre
     * @param controlador controlador de la base de datos
     */
    public VentanaAgregarArma(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;

        initComponentes();
        configurarVentana();
    }

    private void initComponentes() {
        setTitle("Agregar Arma");
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 5, 5, 5);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre del arma:"), gbc);
        gbc.gridx = 1;
        txtNombreArma = new JTextField(20);
        panelForm.add(txtNombreArma, gbc);

        JPanel panelBotones = new JPanel();
        btnGuardar  = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarArma());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void configurarVentana() {
        setSize(320, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
    }

    private void guardarArma() {
        String nombre = txtNombreArma.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del arma es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Arma arma = new Arma(0, nombre);
        if (controlador.agregarArma(arma)) {
            JOptionPane.showMessageDialog(this, "Arma guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarDatosArmas();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el arma.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
