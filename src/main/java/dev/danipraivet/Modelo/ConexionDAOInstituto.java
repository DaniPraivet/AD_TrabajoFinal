package dev.danipraivet.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que realiza las operaciones a la base de datos
 */
public class ConexionDAOInstituto {
    /**
     * URL que contiene el socket y a que base de datos vamos a dirigirnos
     */
    private static final String URL = "jdbc:mysql://localhost:3306/instituto";
    /**
     * Usuario que vamos a usar
     */
    private static final String USER = "root";
    /**
     * Contraseña del usuario
     */
    private static final String PASSWORD = "usuario";

    /**
     * Obtenemos la lista de alumnos de la base de datos
     * @return lista de alumnos
     */
    public static List<Alumno> obtenerAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, estado_matricula, carnet_conducir FROM alumno";
        try (Connection conn = conectarseBD();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Alumno a = new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));
                alumnos.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnos;
    }

    /**
     * Mediante una consulta SELECT obtenemos la lista de las asignaturas
     * @return lista de las asignaturas
     */
    public static List<Asignatura> obtenerAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>(); // Eliminar variable estática
        try (Connection conexionBD = conectarseBD();
                Statement informe = conexionBD.createStatement();
                ResultSet conjuntoResultados = informe.executeQuery("SELECT id, nombre, curso FROM asignatura")) {
            while (conjuntoResultados.next()) {
                int idAsignatura = conjuntoResultados.getInt("id");
                String nombre = conjuntoResultados.getString("nombre");
                int curso = conjuntoResultados.getInt("curso");
                Asignatura a = new Asignatura(idAsignatura, nombre, curso);
                asignaturas.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return asignaturas;
    }

    /**
     *
     * @return Lista de las asignaturas
     */
    public static List<Matricula> obtenerMatriculas() {
        List<Matricula> matriculas = new ArrayList<>(); // Eliminar variable estática
        try (Connection conexionBD = conectarseBD();
                Statement informe = conexionBD.createStatement();
                ResultSet conjuntoResultados = informe.executeQuery("SELECT * FROM matricula")) {
            while (conjuntoResultados.next()) {
                int idMatricula = conjuntoResultados.getInt("id");
                Alumno alumno = obtenerAlumnoPorId(conjuntoResultados.getInt("id_alumno"));
                Asignatura asignatura = obtenerAsignatura(conjuntoResultados.getInt("id_asignatura"));
                double nota = conjuntoResultados.getDouble("nota");
                Matricula m = new Matricula(idMatricula, alumno, asignatura, nota);
                matriculas.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return matriculas;
    }

    /**
     *
     * @param idAlumno id del alumno a encontrar
     * @return alumno
     */
    public static Alumno obtenerAlumnoPorId(int idAlumno) {
        String sql = "SELECT id, nombre, direccion, estado_matricula, carnet_conducir FROM alumno WHERE id = ?";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param idAsignatura id de la asignatura a encontrar
     * @return asignatura
     */
    public static Asignatura obtenerAsignatura(int idAsignatura) {
        String sql = "SELECT id, nombre, curso FROM asignatura WHERE id = ?";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Asignatura(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("curso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param a alumno a insertar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean insertarAlumno(Alumno a) {
        if (a == null || a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO alumno (nombre, direccion, estado_matricula, carnet_conducir) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombre());
            stmt.setString(2, a.getDireccion());
            stmt.setString(3, a.getEstadoMatricula());
            stmt.setInt(4, a.isCarnetConducir() ? 1 : 0);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar alumno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param idAlumno id del alumno a eliminar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean eliminarAlumno(int idAlumno) {
        String sql = "DELETE FROM alumno WHERE id = ?";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param a asignatura a insertar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean insertarAsignatura(Asignatura a) {
        boolean resultado = false;
        String consulta = "INSERT INTO asignatura (nombre, curso) VALUES (?, ?)";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, a.getNombre());
            stmt.setInt(2, a.getCurso());
            resultado = stmt.executeUpdate() >= 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    /**
     *
     * @param idAsignatura id de la asignatura a eliminar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean eliminarAsignatura(int idAsignatura) {
        String sql = "DELETE FROM asignatura WHERE id = ?";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Intenta conectarse a la base de datos mediante el un URL usuario y contraseña establecidos previamente
     * @return la conexion con el servidor
     * @throws SQLException en caso de que ocurra un error se lanza una excepcion tipo SQL
     */
    public static Connection conectarseBD() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     *
     * @param idAlumno id del alumno a buscar
     * @return lista de las matriculas pertenecientes al alumno
     */
    public static List<Matricula> obtenerMatriculasPorAlumno(int idAlumno) {
        List<Matricula> lista = new ArrayList<>();
        String sql = """
                    SELECT m.id, m.nota,
                           a.id AS id_alumno, a.nombre AS nombre_alumno, a.direccion, a.estado_matricula, a.carnet_conducir,
                           asig.id AS id_asig, asig.nombre AS nombre_asig, asig.curso
                    FROM matricula m
                    JOIN alumno a ON m.id_alumno = a.id
                    JOIN asignatura asig ON m.id_asignatura = asig.id
                    WHERE a.id = ?
                """;

        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre_alumno"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));

                Asignatura asignatura = new Asignatura(
                        rs.getInt("id_asig"),
                        rs.getString("nombre_asig"),
                        rs.getInt("curso"));

                Matricula m = new Matricula(
                        rs.getInt("id"),
                        alumno,
                        asignatura,
                        rs.getDouble("nota"));

                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     *
     * @param m matricula a insertar
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean insertarMatricula(Matricula m) {
        String sql = "INSERT INTO matricula (id_alumno, id_asignatura, nota) VALUES (?, ?, ?)";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getAlumno().getId());
            stmt.setInt(2, m.getAsignatura().getId());
            stmt.setDouble(3, m.getNota());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param idAsignatura id de la asignatura a buscar
     * @return lista de las matriculas pertenecientes a esa asignatura
     */
    public static List<Matricula> obtenerMatriculasPorAsignatura(int idAsignatura) {
        List<Matricula> lista = new ArrayList<>();
        String sql = """
                    SELECT m.id, m.nota,
                           a.id AS id_alumno, a.nombre AS nombre_alumno, a.direccion, a.estado_matricula, a.carnet_conducir,
                           asig.id AS id_asig, asig.nombre AS nombre_asig, asig.curso
                    FROM matricula m
                    JOIN alumno a ON m.id_alumno = a.id
                    JOIN asignatura asig ON m.id_asignatura = asig.id
                    WHERE asig.id = ?
                """;

        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre_alumno"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));

                Asignatura asignatura = new Asignatura(
                        rs.getInt("id_asig"),
                        rs.getString("nombre_asig"),
                        rs.getInt("curso"));

                Matricula m = new Matricula(
                        rs.getInt("id"),
                        alumno,
                        asignatura,
                        rs.getDouble("nota"));

                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     *
     * @param idAlumno id del alumno perteneciente a la matricula
     * @param idAsignatura id de la asignatura perteneciente a la matricula
     * @return verdadero o falso dependiendo del éxito del procedimiento o fracaso
     */
    public static boolean eliminarMatricula(int idAlumno, int idAsignatura) {
        String sql = "DELETE FROM matricula WHERE id_alumno = ? AND id_asignatura = ?";
        try (Connection conn = conectarseBD();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idAsignatura);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Comprueba si el usuario y la contrasela están en la tabla de usuarios
     * @param usuario Nombre del usuario para iniciar sesión
     * @param contrasena Contraseña del usuario
     * @return Verdadero o falso en caso de que se encuentre o no
     */
    public static boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";

        try (Connection conn = conectarseBD();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Devuelve verdadero si se encuentra un registro
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
