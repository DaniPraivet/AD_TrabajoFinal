package dev.danipraivet.Modelo;

import javax.swing.*;

/**
 * Clase auxiliar para manejar información de tablas
 */
public class TablaInfo {
    public JTable tabla;
    public String titulo;

    public TablaInfo(JTable tabla, String titulo) {
        this.tabla = tabla;
        this.titulo = titulo;
    }
}