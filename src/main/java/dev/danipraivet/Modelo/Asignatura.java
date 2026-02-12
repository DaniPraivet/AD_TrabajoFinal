package dev.danipraivet.Modelo;

/**
 * Clase asignatura
 */
public class Asignatura {
    /**
     * Identificador de la asignatura
     */
    int id;
    /**
     * Nombre de la asignatura
     */
    String nombre;
    /**
     * Curso en el que se imparte la asignatura
     */
    int curso;

    /**
     * Constructor de la clase asignatura
     * @param id identificador de la asignatura
     * @param nombre nombre de la asignatura
     * @param curso curso en el que se imparte la asignatura
     */
    public Asignatura(int id, String nombre, int curso) {
        this.id = id;
        this.nombre = nombre;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
    return id  + " - " + nombre + " - " + curso + "\n";
    }
}
