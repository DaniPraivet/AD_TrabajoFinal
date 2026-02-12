package dev.danipraivet.Vista;

import instituto.ControladorBBDD.Controlador;
import instituto.Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana donde van a estar la mayoría de funciones por no decir todas
 */
public class VentanaPrincipal extends JFrame {
    /**
     * Controlador para tener acceso a las funciones de la base de datos
     */
    private Controlador controlador;

    private JTable tablaAlumnos;
    private JTable tablaAsignaturas;
    private JTable tablaMatriculas;

    private JTabbedPane menuInterior;
    private JSplitPane panelDivisorCentral;
    private JPanel panelPrincipal;
    private JMenuBar menuBar;
    private JMenu menuAlumno, menuAsignatura, menuMatricula, menuVista, menuPDF;
    private JMenuItem menuItemAgregarAlumno, menuItemEliminarAlumno;
    private JMenuItem menuItemAgregarAsignatura, menuItemEliminarAsignatura;
    private JMenuItem menuItemAgregarMatricula, menuItemEliminarMatricula;
    private JCheckBoxMenuItem menuItemVista;

    private ImageIcon iconPapel, iconAsignatura, iconAlumno, iconMatricula;

    private VisorPDF visorPDF;
    private GestorPDF gestorPDF;

    public VentanaPrincipal() {
        EstiloAplicacion.aplicarEstilo();
        controlador = new Controlador();

        setTitle("Sistema Escolar - Ventana Principal");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initGUI();

        cargarDatosAlumnos();
        cargarDatosAsignaturas();
        cargarDatosMatriculas();
    }

    public void initGUI() {
        iconPapel = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/papellapiz.png");
        iconAsignatura = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/asignatura.png");
        iconAlumno = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/alumno.png");
        iconMatricula = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/matricula.png");

        iconAlumno = new ImageIcon(iconAlumno.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconAsignatura = new ImageIcon(iconAsignatura.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconMatricula = new ImageIcon(iconMatricula.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconPapel = new ImageIcon(iconPapel.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));

        JPanel panelTablaAlumnos = crearPanelAlumnos();
        JPanel panelTablaAsignaturas = crearPanelAsignaturas();
        JPanel panelTablaMatriculas = crearPanelMatriculas();

        menuInterior = new JTabbedPane();
        menuInterior.addTab("Alumnos", iconAlumno, panelTablaAlumnos);
        menuInterior.addTab("Asignatura", iconAsignatura, panelTablaAsignaturas);
        menuInterior.addTab("Matricula", iconMatricula, panelTablaMatriculas);

        panelDivisorCentral = new JSplitPane();
        panelDivisorCentral.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        panelDivisorCentral.setDividerSize(5);
        panelDivisorCentral.setDividerLocation(200);
        panelDivisorCentral.setResizeWeight(0.0);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(Color.LIGHT_GRAY);

        visorPDF = new VisorPDF();
        gestorPDF = new GestorPDF(visorPDF, this);
        panelIzquierdo.add(visorPDF, BorderLayout.CENTER);

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

        menuAlumno = new JMenu("Alumnos");
        menuItemAgregarAlumno = new JMenuItem("Añadir alumno");
        menuItemEliminarAlumno = new JMenuItem("Eliminar alumno");
        menuItemAgregarAlumno.addActionListener(e -> abrirVentanaAgregarAlumno());
        menuItemEliminarAlumno.addActionListener(e -> eliminarAlumnoSeleccionado());
        menuAlumno.add(menuItemAgregarAlumno);
        menuAlumno.add(menuItemEliminarAlumno);

        menuAsignatura = new JMenu("Asignatura");
        menuItemAgregarAsignatura = new JMenuItem("Añadir asignatura");
        menuItemEliminarAsignatura = new JMenuItem("Eliminar asignatura");
        menuItemAgregarAsignatura.addActionListener(e -> abrirVentanaAgregarAsignatura());
        menuItemEliminarAsignatura.addActionListener(e -> eliminarAsignaturaSeleccionada());
        menuAsignatura.add(menuItemAgregarAsignatura);
        menuAsignatura.add(menuItemEliminarAsignatura);

        menuMatricula = new JMenu("Matricula");
        menuItemAgregarMatricula = new JMenuItem("Nueva matrícula");
        menuItemEliminarMatricula = new JMenuItem("Eliminar matrícula");
        menuItemAgregarMatricula.addActionListener(e -> abrirVentanaAgregarMatricula());
        menuItemEliminarMatricula.addActionListener(e -> eliminarMatriculaSeleccionada());
        menuMatricula.add(menuItemAgregarMatricula);
        menuMatricula.add(menuItemEliminarMatricula);

        menuPDF = new JMenu("PDF");

        JMenuItem menuItemGenerarPDFActual = new JMenuItem("Generar PDF de tabla actual");
        menuItemGenerarPDFActual.addActionListener(e -> generarPDFTablaActual());

        JMenuItem menuItemGenerarReporte = new JMenuItem("Generar reporte completo");
        menuItemGenerarReporte.addActionListener(e -> generarReporteCompleto());

        JMenuItem menuItemCargarPDF = new JMenuItem("Cargar PDF existente");
        menuItemCargarPDF.addActionListener(e -> gestorPDF.cargarPDFConDialogo());

        JMenuItem menuItemCerrarPDF = new JMenuItem("Cerrar PDF");
        menuItemCerrarPDF.addActionListener(e -> gestorPDF.cerrarDocumento());

        menuPDF.add(menuItemGenerarPDFActual);
        menuPDF.add(menuItemGenerarReporte);
        menuPDF.addSeparator();
        menuPDF.add(menuItemCargarPDF);
        menuPDF.add(menuItemCerrarPDF);

        menuVista = new JMenu("Vista");
        menuItemVista = new JCheckBoxMenuItem("Vista alumno");
        menuVista.add(menuItemVista);

        menuBar.add(menuAlumno);
        menuBar.add(menuAsignatura);
        menuBar.add(menuMatricula);
        menuBar.add(menuPDF);
        menuBar.add(menuVista);

        setJMenuBar(menuBar);
    }

    private JPanel crearPanelAlumnos() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "Nombre", "Dirección", "Estado Matrícula", "Carnet" };
        tablaAlumnos = new JTable(new DefaultTableModel(columnas, 0));
        RenderizadorCabecera.aplicarEstiloCabeceras(tablaAlumnos);
        JScrollPane scrollPane = new JScrollPane(tablaAlumnos);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAsignaturas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "Nombre", "Curso" };
        tablaAsignaturas = new JTable(new DefaultTableModel(columnas, 0));
        RenderizadorCabecera.aplicarEstiloCabeceras(tablaAsignaturas);
        JScrollPane scrollPane = new JScrollPane(tablaAsignaturas);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMatriculas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "Alumno", "Asignatura", "Nota" };
        tablaMatriculas = new JTable(new DefaultTableModel(columnas, 0));
        RenderizadorCabecera.aplicarEstiloCabeceras(tablaMatriculas);
        JScrollPane scrollPane = new JScrollPane(tablaMatriculas);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void cargarDatosAlumnos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaAlumnos.getModel();
        modelo.setRowCount(0);
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno a : alumnos) {
            modelo.addRow(new Object[] {a.getNombre(), a.getDireccion(), a.getEstadoMatricula(),
                    a.isCarnetConducir() ? "Sí" : "No" });
        }
    }

    public void cargarDatosAsignaturas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaAsignaturas.getModel();
        modelo.setRowCount(0);
        List<Asignatura> asignaturas = controlador.obtenerAsignaturas();
        for (Asignatura a : asignaturas) {
            modelo.addRow(new Object[] {a.getNombre(), a.getCurso() });
        }
    }

    public void cargarDatosMatriculas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaMatriculas.getModel();
        modelo.setRowCount(0);
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno alumno : alumnos) {
            List<Matricula> matriculas = controlador.obtenerMatriculasPorAlumno(alumno.getId());
            for (Matricula m : matriculas) {
                modelo.addRow(new Object[] {m.getAlumno().getNombre(), m.getAsignatura().getNombre(),
                        m.getNota() });
            }
        }
    }

    private void abrirVentanaAgregarAlumno() {
        new VentanaAgregarAlumno(this, controlador).setVisible(true);
    }

    private void abrirVentanaAgregarAsignatura() {
        new VentanaAgregarAsignatura(this, controlador).setVisible(true);
    }

    private void abrirVentanaAgregarMatricula() {
        new VentanaAgregarMatricula(this, controlador).setVisible(true);
    }

    private void eliminarAlumnoSeleccionado() {
        int fila = tablaAlumnos.getSelectedRow();
        if (fila != -1) {
            int id = (int) tablaAlumnos.getValueAt(fila, 0);
            if (controlador.eliminarAlumno(id)) {
                cargarDatosAlumnos();
                cargarDatosMatriculas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar alumno");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar");
        }
    }

    private void eliminarAsignaturaSeleccionada() {
        int fila = tablaAsignaturas.getSelectedRow();
        if (fila != -1) {
            int id = (int) tablaAsignaturas.getValueAt(fila, 0);
            if (controlador.eliminarAsignatura(id)) {
                cargarDatosAsignaturas();
                cargarDatosMatriculas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar asignatura");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una asignatura para eliminar");
        }
    }

    private void eliminarMatriculaSeleccionada() {
        int fila = tablaMatriculas.getSelectedRow();
        String alumnoNombre = (String) tablaMatriculas.getValueAt(fila, 1);
        String asignaturaNombre = (String) tablaMatriculas.getValueAt(fila, 2);
        Alumno alumno = null;
        Asignatura asignatura = null;

        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno a : alumnos) {
            if (a.getNombre().equals(alumnoNombre)) {
                alumno = a;
            }
        }

        List<Asignatura> asignaturas = controlador.obtenerAsignaturas();
        for (Asignatura a : asignaturas) {
            if (a.getNombre().equals(asignaturaNombre)) {
                asignatura = a;
            }
        }

        if (fila != -1) {
            if (controlador.eliminarMatricula(alumno.getId(),  asignatura.getId())) {
                cargarDatosMatriculas();
                JOptionPane.showMessageDialog(this, "Matricula eliminada");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar asignatura");
                JOptionPane.showMessageDialog(this,
                        "Eliminación de matrícula desde aquí requiere refactorización para obtener IDs.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una matrícula para eliminar");
        }
    }

    /**
     * Genera PDF de la tabla actualmente seleccionada
     */
    private void generarPDFTablaActual() {
        int indiceActivo = menuInterior.getSelectedIndex();

        switch (indiceActivo) {
            case 0:
                gestorPDF.generarPDFDesdeTabla(tablaAlumnos, "Listado de Alumnos");
                break;
            case 1:
                gestorPDF.generarPDFDesdeTabla(tablaAsignaturas, "Listado de Asignaturas");
                break;
            case 2:
                gestorPDF.generarPDFDesdeTabla(tablaMatriculas, "Listado de Matrículas");
                break;
        }
    }

    /**
     * Genera un reporte completo con todas las tablas
     */
    private void generarReporteCompleto() {
        TablaInfo[] tablas = {
                new TablaInfo(tablaAlumnos, "Alumnos"),
                new TablaInfo(tablaAsignaturas, "Asignaturas"),
                new TablaInfo(tablaMatriculas, "Matrículas")
        };

        gestorPDF.generarPDFMultiplesTablas("Reporte Completo del Sistema Escolar", tablas);
    }
}