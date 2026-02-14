package dev.danipraivet.Modelo;

/**
 * Clase que representa un arma disponible en el juego
 */
public class Arma {
    /**
     * Identificador del arma
     */
    int id;
    /**
     * Nombre del arma
     */
    String nombreArma;

    /**
     * Constructor de la clase Arma
     * @param id identificador del arma
     * @param nombreArma nombre del arma
     */
    public Arma(int id, String nombreArma) {
        this.id = id;
        this.nombreArma = nombreArma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreArma() {
        return nombreArma;
    }

    public void setNombreArma(String nombreArma) {
        this.nombreArma = nombreArma;
    }

    @Override
    public String toString() {
        return id + " - " + nombreArma;
    }
}
