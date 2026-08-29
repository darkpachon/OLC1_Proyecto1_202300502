package motor;

import ast.Estrategia;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que mantiene el estado actual de un combatiente durante una partida.
 * Incluye vida, recursos, puntuación, historial de movimientos, bonificaciones, etc.
 */
public class EstadoCombatiente {
    
    private Estrategia estrategia;
    private int vida;
    private int recurso;
    private int puntos;
    private List<String> historial;
    
    // --- BONIFICACIONES ACTIVAS ---
    private boolean defendiendose;         // True si está usando SHIELD_BLOCK o MAGIC_BARRIER
    private int bonificacionProximoAtaque; // Bonus de WAR_CRY (+10 al próximo ataque)
    private double valorRandomRonda;       // Valor random asignado al inicio de cada ronda
    
    // --- PARA COMBOS ---
    private int ultimasAccionesCombo;      // Contador para rastrear si hizo un combo
    private boolean comboActivado;         // True si el combo ya se ejecutó esta ronda
    
    public EstadoCombatiente(Estrategia estrategia) {
        this.estrategia = estrategia;
        String clase = estrategia.getClaseEstrategia();
        
        // Inicializar vida y recurso según la clase
        this.vida = Estadisticas.getVidaMaxima(clase);
        this.recurso = Estadisticas.getRecursoMaximo(clase);
        this.puntos = 0;
        this.historial = new ArrayList<>();
        
        this.defendiendose = false;
        this.bonificacionProximoAtaque = 0;
        this.valorRandomRonda = 0.0;
        
        this.ultimasAccionesCombo = 0;
        this.comboActivado = false;
    }
    
    // --- GETTERS Y SETTERS ---
    
    public Estrategia getEstrategia() { return estrategia; }
    
    public int getVida() { return vida; }
    public void setVida(int vida) { 
        int vidaMaxima = Estadisticas.getVidaMaxima(estrategia.getClaseEstrategia());
        this.vida = Math.min(Math.max(vida, 0), vidaMaxima); 
    }
    
    public int getRecurso() { return recurso; }
    public void setRecurso(int recurso) { 
        int recursoMaximo = Estadisticas.getRecursoMaximo(estrategia.getClaseEstrategia());
        this.recurso = Math.min(Math.max(recurso, 0), recursoMaximo); 
    }
    
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = Math.max(0, puntos); }
    public void agregarPuntos(int cantidad) { this.puntos = Math.max(0, this.puntos + cantidad); }
    
    public List<String> getHistorial() { return historial; }
    public void agregarAlHistorial(String accion) { this.historial.add(accion); }
    
    public boolean estaDefiendose() { return defendiendose; }
    public void setDefiendose(boolean defendiendose) { this.defendiendose = defendiendose; }
    
    public int getBonificacionProximoAtaque() { return bonificacionProximoAtaque; }
    public void setBonificacionProximoAtaque(int bonificacion) { this.bonificacionProximoAtaque = bonificacion; }
    public void consumirBonificacionAtaque() { this.bonificacionProximoAtaque = 0; }
    
    public double getValorRandomRonda() { return valorRandomRonda; }
    public void setValorRandomRonda(double valor) { this.valorRandomRonda = valor; }
    
    public int getUltimasAccionesCombo() { return ultimasAccionesCombo; }
    public void setUltimasAccionesCombo(int contador) { this.ultimasAccionesCombo = contador; }
    
    public boolean isComboActivado() { return comboActivado; }
    public void setComboActivado(boolean activado) { this.comboActivado = activado; }
    
    /**
     * Obtiene si el combatiente está vivo.
     */
    public boolean estaVivo() {
        return vida > 0;
    }
    
    /**
     * Obtiene la última acción ejecutada correctamente (o null si el historial está vacío).
     */
    public String obtenerUltimAccion() {
        if (historial.isEmpty()) return null;
        return historial.get(historial.size() - 1);
    }
    
    /**
     * Obtiene una acción del historial por índice (0-based).
     * Retorna null si el índice es inválido.
     */
    public String obtenerAccionPorIndice(int indice) {
        if (indice < 0 || indice >= historial.size()) return null;
        return historial.get(indice);
    }
    
    /**
     * Obtiene las últimas n acciones como lista.
     * Si n es mayor que el historial, retorna todo el historial.
     */
    public List<String> obtenerUltimasNAcciones(int n) {
        int inicio = Math.max(0, historial.size() - n);
        return new ArrayList<>(historial.subList(inicio, historial.size()));
    }
    
    /**
     * Cuenta cuántas veces aparece una acción en el historial.
     */
    public int contarAcciones(String accion) {
        int contador = 0;
        for (String acc : historial) {
            if (acc.equals(accion)) contador++;
        }
        return contador;
    }
    
    /**
     * Verifica si tiene suficiente recurso para ejecutar una acción.
     */
    public boolean puedeEjecutar(Accion accion) {
        return this.recurso >= accion.getCosto();
    }
    
    /**
     * Resetea los estados temporales al inicio de cada ronda.
     */
    public void resetearEstadosTemporales() {
        this.defendiendose = false;
        this.valorRandomRonda = 0.0;
        // La bonificación de WAR_CRY se mantiene hasta que se use
    }
}
