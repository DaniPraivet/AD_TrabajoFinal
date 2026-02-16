package dev.danipraivet.Modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema de logs básico para la aplicación Brawlhalla
 * Registra eventos, errores y acciones importantes en un archivo de texto
 */
public class Logger {

    private static final String LOG_FILE = "brawlhalla_app.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Logger instancia;

    /**
     * Constructor privado para patrón Singleton
     */
    private Logger() {
        // Inicialización si es necesaria
    }

    /**
     * Obtiene la instancia única del Logger
     * @return instancia del Logger
     */
    public static Logger getInstance() {
        if (instancia == null) {
            instancia = new Logger();
        }
        return instancia;
    }

    /**
     * Registra un mensaje de información
     * @param mensaje mensaje a registrar
     */
    public void info(String mensaje) {
        escribirLog("INFO", mensaje);
    }

    /**
     * Registra un mensaje de advertencia
     * @param mensaje mensaje a registrar
     */
    public void warning(String mensaje) {
        escribirLog("WARNING", mensaje);
    }

    /**
     * Registra un mensaje de error
     * @param mensaje mensaje a registrar
     */
    public void error(String mensaje) {
        escribirLog("ERROR", mensaje);
    }

    /**
     * Registra un error con su excepción
     * @param mensaje mensaje descriptivo
     * @param e excepción ocurrida
     */
    public void error(String mensaje, Exception e) {
        escribirLog("ERROR", mensaje + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    /**
     * Registra un mensaje de depuración
     * @param mensaje mensaje a registrar
     */
    public void debug(String mensaje) {
        escribirLog("DEBUG", mensaje);
    }

    /**
     * Escribe el log en el archivo
     * @param nivel nivel de log (INFO, WARNING, ERROR, DEBUG)
     * @param mensaje mensaje a registrar
     */
    private void escribirLog(String nivel, String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = String.format("[%s] [%s] %s", timestamp, nivel, mensaje);

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        }

        // También imprimir en consola
        System.out.println(logEntry);
    }

    /**
     * Registra el inicio de sesión de un usuario
     * @param usuario nombre del usuario
     */
    public void loginExitoso(String usuario) {
        info("Login exitoso - Usuario: " + usuario);
    }

    /**
     * Registra un intento fallido de inicio de sesión
     * @param usuario nombre del usuario
     */
    public void loginFallido(String usuario) {
        warning("Intento de login fallido - Usuario: " + usuario);
    }

    /**
     * Registra operaciones CRUD
     * @param operacion tipo de operación (INSERT, UPDATE, DELETE)
     * @param entidad entidad afectada (Leyenda, Arma)
     * @param detalle detalle adicional
     */
    public void operacionBD(String operacion, String entidad, String detalle) {
        info(String.format("BD %s - %s: %s", operacion, entidad, detalle));
    }
}