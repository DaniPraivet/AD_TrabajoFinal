package dev.danipraivet.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que realiza todas las operaciones contra la base de datos brawlhalla
 */
public class ConexionDAOBrawlhalla {

    /** URL de conexión a la base de datos */
    private static final String URL      = "jdbc:mysql://localhost:3306/brawlhalla";
    /** Usuario de MySQL */
    private static final String USER     = "root";
    /** Contraseña de MySQL */
    private static final String PASSWORD = "usuario";


    /**
     * Abre y devuelve una conexión a la base de datos
     * @return Connection activa
     * @throws SQLException si no se puede conectar
     */
    public static Connection conectarseBD() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    /**
     * Obtiene la lista completa de armas
     * @return lista de Arma
     */
    public static List<Arma> obtenerArmas() {
        List<Arma> armas = new ArrayList<>();
        String sql = "SELECT id, nombre_arma FROM armas";
        try (Connection conn = conectarseBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                armas.add(new Arma(rs.getInt("id"), rs.getString("nombre_arma")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return armas;
    }

    /**
     * Busca un arma por su id
     * @param idArma id del arma
     * @return Arma encontrada o null
     */
    public static Arma obtenerArmaPorId(int idArma) {
        String sql = "SELECT id, nombre_arma FROM armas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArma);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Arma(rs.getInt("id"), rs.getString("nombre_arma"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserta un arma nueva en la base de datos
     * @param a arma a insertar
     * @return true si se insertó correctamente
     */
    public static boolean insertarArma(Arma a) {
        if (a == null || a.getNombreArma() == null || a.getNombreArma().trim().isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO armas (nombre_arma) VALUES (?)";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombreArma().trim());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) a.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar arma: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un arma por su id
     * @param idArma id del arma a eliminar
     * @return true si se eliminó correctamente
     */
    public static boolean eliminarArma(int idArma) {
        String sql = "DELETE FROM armas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArma);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Obtiene la lista completa de leyendas (con sus dos armas resueltas)
     * @return lista de Leyenda
     */
    public static List<Leyenda> obtenerLeyendas() {
        List<Leyenda> leyendas = new ArrayList<>();
        String sql = """
                SELECT l.id, l.nombre, l.vida, l.fuerza, l.velocidad, l.destreza, l.defensa,
                       a1.id AS id_a1, a1.nombre_arma AS nombre_a1,
                       a2.id AS id_a2, a2.nombre_arma AS nombre_a2
                FROM leyendas l
                JOIN armas a1 ON l.id_arma1 = a1.id
                JOIN armas a2 ON l.id_arma2 = a2.id
                """;
        try (Connection conn = conectarseBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Arma arma1 = new Arma(rs.getInt("id_a1"), rs.getString("nombre_a1"));
                Arma arma2 = new Arma(rs.getInt("id_a2"), rs.getString("nombre_a2"));
                leyendas.add(new Leyenda(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("vida"),
                        rs.getInt("fuerza"),
                        rs.getInt("velocidad"),
                        rs.getInt("destreza"),
                        rs.getInt("defensa"),
                        arma1, arma2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leyendas;
    }

    /**
     * Busca una leyenda por su id
     * @param idLeyenda id de la leyenda
     * @return Leyenda encontrada o null
     */
    public static Leyenda obtenerLeyendaPorId(int idLeyenda) {
        String sql = """
                SELECT l.id, l.nombre, l.vida, l.fuerza, l.velocidad, l.destreza, l.defensa,
                       a1.id AS id_a1, a1.nombre_arma AS nombre_a1,
                       a2.id AS id_a2, a2.nombre_arma AS nombre_a2
                FROM leyendas l
                JOIN armas a1 ON l.id_arma1 = a1.id
                JOIN armas a2 ON l.id_arma2 = a2.id
                WHERE l.id = ?
                """;
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLeyenda);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Arma arma1 = new Arma(rs.getInt("id_a1"), rs.getString("nombre_a1"));
                Arma arma2 = new Arma(rs.getInt("id_a2"), rs.getString("nombre_a2"));
                return new Leyenda(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("vida"),
                        rs.getInt("fuerza"),
                        rs.getInt("velocidad"),
                        rs.getInt("destreza"),
                        rs.getInt("defensa"),
                        arma1, arma2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserta una nueva leyenda en la base de datos
     * @param l leyenda a insertar
     * @return true si se insertó correctamente
     */
    public static boolean insertarLeyenda(Leyenda l) {
        if (l == null || l.getNombre() == null || l.getNombre().trim().isEmpty()) {
            return false;
        }
        String sql = """
                INSERT INTO leyendas (nombre, vida, fuerza, velocidad, destreza, defensa, id_arma1, id_arma2)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, l.getNombre().trim());
            stmt.setInt(2, l.getVida());
            stmt.setInt(3, l.getFuerza());
            stmt.setInt(4, l.getVelocidad());
            stmt.setInt(5, l.getDestreza());
            stmt.setInt(6, l.getDefensa());
            stmt.setInt(7, l.getArma1().getId());
            stmt.setInt(8, l.getArma2().getId());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) l.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar leyenda: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina una leyenda por su id
     * @param idLeyenda id de la leyenda a eliminar
     * @return true si se eliminó correctamente
     */
    public static boolean eliminarLeyenda(int idLeyenda) {
        String sql = "DELETE FROM leyendas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLeyenda);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Valida credenciales de acceso
     * @param usuario nombre de usuario
     * @param contrasena contraseña
     * @return true si las credenciales son correctas
     */
    public static boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}
