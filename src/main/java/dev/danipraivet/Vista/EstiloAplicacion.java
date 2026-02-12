package dev.danipraivet.Vista;

import javax.swing.*;
import java.awt.*;

/**
 * Clase para modificar rápidamente el estilo que vayamos a usar en nuestro programa
 */
public class EstiloAplicacion {
    public static void aplicarEstilo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.put("Table.gridColor", new Color(200, 200, 200));
            UIManager.put("Table.selectionBackground", new Color(100, 149, 237));
            UIManager.put("Table.selectionForeground", Color.WHITE);

            UIManager.put("Button.background", new Color(240, 248, 255));
            UIManager.put("Button.foreground", new Color(0, 0, 139));
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 11));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
