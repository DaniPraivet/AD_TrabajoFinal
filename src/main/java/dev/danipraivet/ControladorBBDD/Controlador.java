package dev.danipraivet.ControladorBBDD;

import dev.danipraivet.Modelo.Arma;
import dev.danipraivet.Modelo.Leyenda;
import dev.danipraivet.Modelo.ConexionDAOBrawlhalla;

import java.util.List;

/**
 * Clase intermediaria que procesa los datos obtenidos en ConexionDAOBrawlhalla
 */
public class Controlador {

    // ─────────────────────────────────────────────
    //  LEYENDAS
    // ─────────────────────────────────────────────

    /**
     * Obtiene la lista completa de leyendas
     * @return lista de leyendas
     */
    public List<Leyenda> obtenerLeyendas() {
        try {
            return ConexionDAOBrawlhalla.obtenerLeyendas();
        } catch (Exception e) {
            System.err.println("Error al obtener leyendas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Inserta una nueva leyenda en la base de datos
     * @param l leyenda a insertar
     * @return true si se insertó correctamente
     */
    public boolean agregarLeyenda(Leyenda l) {
        if (l == null) {
            System.out.println("Error: Leyenda nula");
            return false;
        }
        if (l.getNombre() == null || l.getNombre().trim().isEmpty()) {
            System.out.println("Error: Nombre de leyenda inválido");
            return false;
        }
        return ConexionDAOBrawlhalla.insertarLeyenda(l);
    }

    /**
     * Elimina una leyenda por su id
     * @param idLeyenda id de la leyenda a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarLeyenda(int idLeyenda) {
        return ConexionDAOBrawlhalla.eliminarLeyenda(idLeyenda);
    }

    // ─────────────────────────────────────────────
    //  ARMAS
    // ─────────────────────────────────────────────

    /**
     * Obtiene la lista completa de armas
     * @return lista de armas
     */
    public List<Arma> obtenerArmas() {
        return ConexionDAOBrawlhalla.obtenerArmas();
    }

    /**
     * Inserta un arma nueva en la base de datos
     * @param a arma a insertar
     * @return true si se insertó correctamente
     */
    public boolean agregarArma(Arma a) {
        return ConexionDAOBrawlhalla.insertarArma(a);
    }

    /**
     * Elimina un arma por su id
     * @param idArma id del arma a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarArma(int idArma) {
        return ConexionDAOBrawlhalla.eliminarArma(idArma);
    }
}
