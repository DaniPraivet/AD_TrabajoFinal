package dev.danipraivet.Modelo;

/**
 * Clase matricula
 */
public class Matricula {
    /**
     * Identificador de la matricula
     */
    int id;
    /**
     * Alumno asignado a la matricula
     */
    Alumno alumno;
    /**
     * Asignatura asignada a la matricula
     */
    Asignatura asignatura;
    /**
     * Nota asignada a la matricula
     */
    double nota;

    /**
     * Constructor principal
     * @param id identificador
     * @param alumno alumno
     * @param asignatura asignatura
     * @param nota nota
     */
    public Matricula(int id, Alumno alumno, Asignatura asignatura, double nota) {
        this.id = id;
        this.alumno = alumno;
        this.asignatura = asignatura;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
