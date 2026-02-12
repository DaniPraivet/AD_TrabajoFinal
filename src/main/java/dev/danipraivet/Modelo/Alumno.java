package dev.danipraivet.Modelo;

/**
 * Clase alumno
 */
public class Alumno {
    /**
     * Id del alumno
     */
    int id;
    /**
     * Nombre del alumno
     */
    String nombre;
    /**
     * Dirección o domicilio del alumno
     */
    String direccion;
    /**
     * Estado de la matrícula del alumno
     */
    String estadoMatricula;
    /**
     * Si el alumno tiene carnet de conducir o no
     */
    boolean carnetConducir;

    /**
     * Constructor base de la clase alumno
     * @param id id del alumno
     * @param nombre nombre del alumno
     * @param direccion dirección o domicilio del alumno
     * @param estadoMatricula estado de la matricula del alumno
     * @param carnetConducir si tiene carnet de conducir o no
     */
    public Alumno(int id, String nombre, String direccion, String estadoMatricula, int carnetConducir) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.estadoMatricula = estadoMatricula;
        this.carnetConducir = (carnetConducir == 1);
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstadoMatricula() {
        return estadoMatricula;
    }

    public void setEstadoMatricula(String estadoMatricula) {
        this.estadoMatricula = estadoMatricula;
    }

    public boolean isCarnetConducir() {
        return carnetConducir;
    }

    public void setCarnetConducir(boolean carnetConducir) {
        this.carnetConducir = carnetConducir;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " - " + direccion + " - " + estadoMatricula + " - " + (carnetConducir ? "Sí" : "No");
    }
}