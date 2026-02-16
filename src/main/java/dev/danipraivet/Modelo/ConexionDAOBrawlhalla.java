package dev.danipraivet.Modelo;

import dev.danipraivet.Util.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que realiza todas las operaciones contra la base de datos brawlhalla
 * VERSIÓN CON LOGS INTEGRADOS
 */
public class ConexionDAOBrawlhalla {

    private static final Logger logger = Logger.getInstance();

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
        logger.debug("Intentando conectar a la base de datos");
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.debug("Conexión a BD establecida correctamente");
            return conn;
        } catch (SQLException e) {
            logger.error("Error al conectar a la base de datos", e);
            throw e;
        }
    }


    /**
     * Obtiene la lista completa de armas
     * @return lista de Arma
     */
    public static List<Arma> obtenerArmas() {
        logger.info("Obteniendo lista de armas");
        List<Arma> armas = new ArrayList<>();
        String sql = "SELECT id, nombre_arma FROM armas";
        try (Connection conn = conectarseBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                armas.add(new Arma(rs.getInt("id"), rs.getString("nombre_arma")));
            }
            logger.info("Se obtuvieron " + armas.size() + " armas");
        } catch (SQLException e) {
            logger.error("Error al obtener armas", e);
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
        logger.debug("Buscando arma con ID: " + idArma);
        String sql = "SELECT id, nombre_arma FROM armas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArma);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Arma arma = new Arma(rs.getInt("id"), rs.getString("nombre_arma"));
                logger.debug("Arma encontrada: " + arma.getNombreArma());
                return arma;
            }
            logger.warning("No se encontró arma con ID: " + idArma);
        } catch (SQLException e) {
            logger.error("Error al buscar arma por ID", e);
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
            logger.warning("Intento de insertar arma nula o con nombre vacío");
            return false;
        }
        logger.info("Insertando nueva arma: " + a.getNombreArma());
        String sql = "INSERT INTO armas (nombre_arma) VALUES (?)";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombreArma().trim());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                    logger.operacionBD("INSERT", "Arma", "ID: " + a.getId() + ", Nombre: " + a.getNombreArma());
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al insertar arma: " + a.getNombreArma(), e);
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
        logger.info("Eliminando arma con ID: " + idArma);
        String sql = "DELETE FROM armas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArma);
            boolean resultado = stmt.executeUpdate() > 0;
            if (resultado) {
                logger.operacionBD("DELETE", "Arma", "ID eliminado: " + idArma);
            } else {
                logger.warning("No se pudo eliminar arma con ID: " + idArma);
            }
            return resultado;
        } catch (SQLException e) {
            logger.error("Error al eliminar arma con ID: " + idArma, e);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Obtiene la lista completa de leyendas (con sus dos armas resueltas)
     * @return lista de Leyenda
     */
    public static List<Leyenda> obtenerLeyendas() {
        logger.info("Obteniendo lista de leyendas");
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
            logger.info("Se obtuvieron " + leyendas.size() + " leyendas");
        } catch (SQLException e) {
            logger.error("Error al obtener leyendas", e);
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
        logger.debug("Buscando leyenda con ID: " + idLeyenda);
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
                Leyenda leyenda = new Leyenda(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("vida"),
                        rs.getInt("fuerza"),
                        rs.getInt("velocidad"),
                        rs.getInt("destreza"),
                        rs.getInt("defensa"),
                        arma1, arma2);
                logger.debug("Leyenda encontrada: " + leyenda.getNombre());
                return leyenda;
            }
            logger.warning("No se encontró leyenda con ID: " + idLeyenda);
        } catch (SQLException e) {
            logger.error("Error al buscar leyenda por ID", e);
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
            logger.warning("Intento de insertar leyenda nula o con nombre vacío");
            return false;
        }
        logger.info("Insertando nueva leyenda: " + l.getNombre());
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
                if (rs.next()) {
                    l.setId(rs.getInt(1));
                    logger.operacionBD("INSERT", "Leyenda",
                            String.format("ID: %d, Nombre: %s, Armas: %s/%s",
                                    l.getId(), l.getNombre(),
                                    l.getArma1().getNombreArma(),
                                    l.getArma2().getNombreArma()));
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al insertar leyenda: " + l.getNombre(), e);
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
        logger.info("Eliminando leyenda con ID: " + idLeyenda);
        String sql = "DELETE FROM leyendas WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLeyenda);
            boolean resultado = stmt.executeUpdate() > 0;
            if (resultado) {
                logger.operacionBD("DELETE", "Leyenda", "ID eliminado: " + idLeyenda);
            } else {
                logger.warning("No se pudo eliminar leyenda con ID: " + idLeyenda);
            }
            return resultado;
        } catch (SQLException e) {
            logger.error("Error al eliminar leyenda con ID: " + idLeyenda, e);
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
        logger.info("Intento de validación de usuario: " + usuario);
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean valido = rs.next();
                if (valido) {
                    logger.loginExitoso(usuario);
                } else {
                    logger.loginFallido(usuario);
                }
                return valido;
            }
        } catch (SQLException e) {
            logger.error("Error al validar usuario: " + usuario, e);
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}