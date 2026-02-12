package dev.danipraivet.ControladorBBDD;

import instituto.Modelo.Alumno;
import instituto.Modelo.Asignatura;
import instituto.Modelo.Matricula;
import instituto.Modelo.ConexionDAOInstituto;

import java.util.Comparator;
import java.util.List;

/**
 * Clase intermediaria que procesa los datos obtenidos en ConexionDAOInstituto
 */
public class Controlador {

    // ALUMNOS

    /**
     * Obtener la lista de alumnos de la base de datos
     * @return retorna la lista de los alumnos
     */
    public List<Alumno> obtenerAlumnos() {
        try {
            return ConexionDAOInstituto.obtenerAlumnos();
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
            return List.of(); // Devolver lista vacía en lugar de null
        }
    }

    /**
     * Insertar un nuevo alumno en la base de datos
     * @param a Alumno a insertar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean agregarAlumno(Alumno a) {
        if (a == null) {
            System.out.println("Error: Alumno nulo");
            return false;
        }
        if (a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            System.out.println("Error: Nombre de alumno inválido");
            return false;
        }
        return ConexionDAOInstituto.insertarAlumno(a);
    }

    /**
     * Eliminar un alumno de la base de datos
     * @param idAlumno id del alumno a eliminar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean eliminarAlumno(int idAlumno) {
        return ConexionDAOInstituto.eliminarAlumno(idAlumno);
    }

    // ASIGNATURAS

    /**
     * Obtener la lista de asignaturas de la base de datos
     * @return la lista de las asignaturas
     */
    public List<Asignatura> obtenerAsignaturas() {
        return ConexionDAOInstituto.obtenerAsignaturas();
    }

    /**
     * Inserta una asignatura a la base de datos
     * @param a asignatura a agregar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean agregarAsignatura(Asignatura a) {
        return ConexionDAOInstituto.insertarAsignatura(a);
    }

    /**
     * Eliminar una asignatura de la base de datos
     * @param idAsignatura id de la asignatura a eliminar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean eliminarAsignatura(int idAsignatura) {
        return ConexionDAOInstituto.eliminarAsignatura(idAsignatura);
    }

    // MATRICULAS

    /**
     * Devuelve una lista de las matrículas que pertenezcan a dicho alumno
     * @param idAlumno id del alumno
     * @return lista de las matriculas obtenidas
     */
    public List<Matricula> obtenerMatriculasPorAlumno(int idAlumno) {
        return ConexionDAOInstituto.obtenerMatriculasPorAlumno(idAlumno);
    }

    /**
     * Insertar una matricula en la base de datos
     * @param m matricula a agregar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean insertarMatricula(Matricula m) {
        return ConexionDAOInstituto.insertarMatricula(m);
    }

    /**
     * Eliminar una matricula en la base de datos
     * @param idAlumno id del alumno perteneciente a la matricula
     * @param idAsignatura id de la asignatura perteneciente a la matricula
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public boolean eliminarMatricula(int idAlumno, int idAsignatura) {
        return ConexionDAOInstituto.eliminarMatricula(idAlumno, idAsignatura);
    }

}