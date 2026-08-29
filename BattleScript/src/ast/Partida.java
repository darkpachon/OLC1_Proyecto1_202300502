package ast;

import java.util.List;

/**
 * Clase que representa la configuración de un Match (Partida) dentro del AST.
 * Guarda los participantes, rondas, reglas de puntuación y bonificaciones.
 */
public class Partida {
    
    private String nombre;
    private List<String> jugadores; // Lista que contendrá los nombres de las 2 estrategias
    private int rondas;

    // --- Atributos de Puntuación (Scoring) ---
    private int damagePoint;
    private int healingPoint;
    private int successfulDefense;
    private int victoryBonus;
    private int failedActionPenalty;

    // --- Atributos de Bonificaciones (Bonuses) ---
    private List<String> mageCombo; // Secuencia de acciones del combo de mago
    private int mageComboPoints;
    private List<String> warriorCombo; // Secuencia de acciones del combo de guerrero
    private int warriorComboPoints;
    private int lowHealthVictory;

    /**
     * Constructor principal de la Partida.
     * 
     * @param nombre El identificador único de la partida (ej. DueloInicial).
     */
    public Partida(String nombre) {
        this.nombre = nombre;
    }

    // --- GETTERS Y SETTERS ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<String> jugadores) {
        this.jugadores = jugadores;
    }

    public int getRondas() {
        return rondas;
    }

    public void setRondas(int rondas) {
        this.rondas = rondas;
    }

    public int getDamagePoint() {
        return damagePoint;
    }

    public void setDamagePoint(int damagePoint) {
        this.damagePoint = damagePoint;
    }

    public int getHealingPoint() {
        return healingPoint;
    }

    public void setHealingPoint(int healingPoint) {
        this.healingPoint = healingPoint;
    }

    public int getSuccessfulDefense() {
        return successfulDefense;
    }

    public void setSuccessfulDefense(int successfulDefense) {
        this.successfulDefense = successfulDefense;
    }

    public int getVictoryBonus() {
        return victoryBonus;
    }

    public void setVictoryBonus(int victoryBonus) {
        this.victoryBonus = victoryBonus;
    }

    public int getFailedActionPenalty() {
        return failedActionPenalty;
    }

    public void setFailedActionPenalty(int failedActionPenalty) {
        this.failedActionPenalty = failedActionPenalty;
    }

    public List<String> getMageCombo() {
        return mageCombo;
    }

    public void setMageCombo(List<String> mageCombo) {
        this.mageCombo = mageCombo;
    }

    public int getMageComboPoints() {
        return mageComboPoints;
    }

    public void setMageComboPoints(int mageComboPoints) {
        this.mageComboPoints = mageComboPoints;
    }

    public List<String> getWarriorCombo() {
        return warriorCombo;
    }

    public void setWarriorCombo(List<String> warriorCombo) {
        this.warriorCombo = warriorCombo;
    }

    public int getWarriorComboPoints() {
        return warriorComboPoints;
    }

    public void setWarriorComboPoints(int warriorComboPoints) {
        this.warriorComboPoints = warriorComboPoints;
    }

    public int getLowHealthVictory() {
        return lowHealthVictory;
    }

    public void setLowHealthVictory(int lowHealthVictory) {
        this.lowHealthVictory = lowHealthVictory;
    }

    /**
     * Método auxiliar para imprimir los datos de la partida en consola.
     */
    @Override
    public String toString() {
        return "Partida [Nombre: " + nombre + 
               ", Jugadores: " + jugadores + 
               ", Rondas: " + rondas + "]\n" +
               "  Scoring [Daño: " + damagePoint + ", Curación: " + healingPoint + " ...]\n" +
               "  Bonuses [MageCombo: " + mageCombo + ", WarriorCombo: " + warriorCombo + " ...]";
    }
}