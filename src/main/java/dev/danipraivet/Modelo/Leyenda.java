package dev.danipraivet.Modelo;

/**
 * Clase que representa una leyenda del juego Brawlhalla
 */
public class Leyenda {
    /**
     * Id de la leyenda
     */
    int id;
    /**
     * Nombre de la leyenda
     */
    String nombre;
    /**
     * Estadística de vida
     */
    int vida;
    /**
     * Estadística de fuerza
     */
    int fuerza;
    /**
     * Estadística de velocidad
     */
    int velocidad;
    /**
     * Estadística de destreza
     */
    int destreza;
    /**
     * Estadística de defensa
     */
    int defensa;
    /**
     * Primera arma equipada por la leyenda
     */
    Arma arma1;
    /**
     * Segunda arma equipada por la leyenda
     */
    Arma arma2;

    /**
     * Constructor completo de la clase Leyenda
     * @param id identificador de la leyenda
     * @param nombre nombre de la leyenda
     * @param vida estadística de vida
     * @param fuerza estadística de fuerza
     * @param velocidad estadística de velocidad
     * @param destreza estadística de destreza
     * @param defensa estadística de defensa
     * @param arma1 primera arma equipada
     * @param arma2 segunda arma equipada
     */
    public Leyenda(int id, String nombre, int vida, int fuerza, int velocidad,
                   int destreza, int defensa, Arma arma1, Arma arma2) {
        this.id = id;
        this.nombre = nombre;
        this.vida = vida;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.destreza = destreza;
        this.defensa = defensa;
        this.arma1 = arma1;
        this.arma2 = arma2;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { this.fuerza = fuerza; }

    public int getVelocidad() { return velocidad; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }

    public int getDestreza() { return destreza; }
    public void setDestreza(int destreza) { this.destreza = destreza; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public Arma getArma1() { return arma1; }
    public void setArma1(Arma arma1) { this.arma1 = arma1; }

    public Arma getArma2() { return arma2; }
    public void setArma2(Arma arma2) { this.arma2 = arma2; }

    @Override
    public String toString() {
        return id + " - " + nombre
                + " | Vida:" + vida + " Fuerza:" + fuerza
                + " Vel:" + velocidad + " Dest:" + destreza + " Def:" + defensa
                + " | " + (arma1 != null ? arma1.getNombreArma() : "?")
                + " / " + (arma2 != null ? arma2.getNombreArma() : "?");
    }
}
