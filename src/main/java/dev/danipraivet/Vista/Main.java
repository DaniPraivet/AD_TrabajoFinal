package dev.danipraivet.Vista;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

/**
 * Clase principal — arranca el gestor de Brawlhalla
 */
public class Main {
    /**
     * Aplica el look and feel y abre la ventana de login
     * @param args argumentos adicionales
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}