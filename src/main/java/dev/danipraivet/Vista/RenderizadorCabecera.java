package dev.danipraivet.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Clase encargada de estilizar las cabeceras de las tablas
 */
public class RenderizadorCabecera extends DefaultTableCellRenderer {
    public RenderizadorCabecera() {
        setHorizontalAlignment(CENTER);
        setOpaque(true);
        setBackground(new Color(30, 144, 255));
        setForeground(Color.WHITE);
    }
    public static void aplicarEstiloCabeceras(JTable tabla) {
        tabla.getTableHeader().setDefaultRenderer(new RenderizadorCabecera());
    }
}
