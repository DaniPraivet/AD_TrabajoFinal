package dev.danipraivet.Vista;

import instituto.ControladorBBDD.Controlador;
import instituto.Modelo.Asignatura;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana emergente para agregar una nueva asignatura
 */
public class VentanaAgregarAsignatura extends JFrame {
    /**
     * Campo donde rellenar el nombre
     */
    private JTextField txtNombre;
    /**
     * Campo donde rellenar el curso
     */
    private JTextField txtCurso;
    /**
     * Botón para guardar los cambios y agregar la asignatura
     */
    private JButton btnGuardar;
    /**
     * Botón para cancelar la operación y cerrar la ventana
     */
    private JButton btnCancelar;
    /**
     * Componente padre
     */
    private VentanaPrincipal ventanaPrincipal;
    /**
     * Controlador para acceder a los métodos de la base de datos
     */
    private Controlador controlador;

    /**
     * Constructor principal
     * @param ventanaPrincipal componente padre
     * @param controlador controlador
     */
    public VentanaAgregarAsignatura(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;

        initComponentes();
        configurarVentana();
    }

    /**
     * Metodo donde iniciamos los componentes para luego mostrarlos por pantalla
     */
    private void initComponentes() {
        setTitle("Agregar Asignatura");
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        txtCurso = new JTextField(20);
        panelForm.add(txtCurso, gbc);

        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarAsignatura());
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Ajustes básicos para la ventana
     */
    private void configurarVentana() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
    }

    /**
     * Comprobamos que se han rellenado los campos correctamente y si lo hemos rellenado correctamente lanzamos la consulta a la base de datos
     */
    private void guardarAsignatura() {
        String nombre = txtNombre.getText().trim();
        String cursoStr = txtCurso.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int curso = 0;
        try {
            curso = Integer.parseInt(cursoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El curso debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Asignatura asignatura = new Asignatura(0, nombre, curso);
        if (controlador.agregarAsignatura(asignatura)) {
            JOptionPane.showMessageDialog(this, "Asignatura guardada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarDatosAsignaturas();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar asignatura", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
