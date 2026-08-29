package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una Estrategia dentro del Árbol de Sintaxis Abstracta (AST).
 * Guarda la definición de un combatiente para ser evaluada luego por el motor de batallas.
 */
public class Estrategia {
    
    private String nombre;
    private String claseEstrategia; // Puede ser "mage" o "warrior"
    private String accionInicial;
    private List<Regla> reglas;
    private String accionPorDefecto;

    /**
     * Constructor principal de la Estrategia.
     * Inicializa la lista de reglas vacía por defecto.
     * 
     * @param nombre El identificador único de la estrategia (ej. Merlin, Ragnar).
     * @param claseEstrategia El tipo de combatiente ("mage" o "warrior").
     */
    public Estrategia(String nombre, String claseEstrategia) {
        this.nombre = nombre;
        this.claseEstrategia = claseEstrategia;
        this.reglas = new ArrayList<>();
    }

    // --- GETTERS Y SETTERS ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClaseEstrategia() {
        return claseEstrategia;
    }

    public void setClaseEstrategia(String claseEstrategia) {
        this.claseEstrategia = claseEstrategia;
    }

    public String getAccionInicial() {
        return accionInicial;
    }

    public void setAccionInicial(String accionInicial) {
        this.accionInicial = accionInicial;
    }

    public List<Regla> getReglas() {
        return reglas;
    }

    public void setReglas(List<Regla> reglas) {
        this.reglas = reglas;
    }

    /**
     * Agrega una nueva regla if-then a la colección ordenada de reglas.
     * Las reglas se evaluarán en cascada según el orden en que se agreguen.
     * 
     * @param regla La regla a agregar.
     */
    public void agregarRegla(Regla regla) {
        this.reglas.add(regla);
    }

    public String getAccionPorDefecto() {
        return accionPorDefecto;
    }

    public void setAccionPorDefecto(String accionPorDefecto) {
        this.accionPorDefecto = accionPorDefecto;
    }

    /**
     * Método auxiliar para imprimir los datos de la estrategia en consola.
     * Muy útil para verificar que el Parser está guardando los datos correctamente.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estrategia [Nombre: ").append(nombre)
          .append(", Clase: ").append(claseEstrategia)
          .append(", Inicial: ").append(accionInicial)
          .append("]\n");
        
        sb.append("  Reglas:\n");
        for (Regla r : reglas) {
            sb.append("    - ").append(r.toString()).append("\n");
        }
        sb.append("  Defecto (else): ").append(accionPorDefecto).append("\n");
        
        return sb.toString();
    }
}
