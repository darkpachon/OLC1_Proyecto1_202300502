package motor;

/**
 * Enumeración que define todas las acciones disponibles en el juego.
 * Cada acción tiene sus propiedades: tipo, poder, costo y prioridad.
 */
public enum Accion {
    
    // --- ACCIONES DEL MAGO ---
    ARCANE_BOLT("ARCANE_BOLT", "Ataque mágico", 12, 10, 4, "mage"),
    FIREBALL("FIREBALL", "Ataque mágico", 25, 30, 2, "mage"),
    MAGIC_BARRIER("MAGIC_BARRIER", "Defensa", 0, 20, 7, "mage"),
    HEALING_RUNE("HEALING_RUNE", "Curación", 25, 30, 5, "mage"),
    MEDITATE("MEDITATE", "Recuperación", 25, 0, 1, "mage"),
    
    // --- ACCIONES DEL GUERRERO ---
    SLASH("SLASH", "Ataque físico", 12, 10, 4, "warrior"),
    HEAVY_STRIKE("HEAVY_STRIKE", "Ataque físico", 25, 25, 2, "warrior"),
    SHIELD_BLOCK("SHIELD_BLOCK", "Defensa", 0, 15, 7, "warrior"),
    WAR_CRY("WAR_CRY", "Mejora", 10, 20, 6, "warrior"),
    REST("REST", "Recuperación", 25, 0, 1, "warrior");
    
    private String nombre;
    private String tipo;          // Tipo de acción: Ataque físico, Ataque mágico, Defensa, Curación, Recuperación, Mejora
    private int poder;            // Daño o curación base
    private int costo;            // Costo en maná (mago) o energía (guerrero)
    private int prioridad;        // Orden de ejecución (mayor = ejecuta primero)
    private String claseValida;   // "mage" o "warrior"
    
    Accion(String nombre, String tipo, int poder, int costo, int prioridad, String claseValida) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.poder = poder;
        this.costo = costo;
        this.prioridad = prioridad;
        this.claseValida = claseValida;
    }
    
    // --- GETTERS ---
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getPoder() { return poder; }
    public int getCosto() { return costo; }
    public int getPrioridad() { return prioridad; }
    public String getClaseValida() { return claseValida; }
    
    /**
     * Verifica si la acción es un ataque.
     */
    public boolean esAtaque() {
        return tipo.contains("Ataque");
    }
    
    /**
     * Verifica si la acción es curación.
     */
    public boolean esCuracion() {
        return tipo.equals("Curación");
    }
    
    /**
     * Verifica si la acción es defensa.
     */
    public boolean esDefensa() {
        return tipo.equals("Defensa");
    }
    
    /**
     * Verifica si la acción es recuperación de recursos.
     */
    public boolean esRecuperacion() {
        return tipo.equals("Recuperación");
    }
    
    /**
     * Verifica si la acción es mejora.
     */
    public boolean esMejora() {
        return tipo.equals("Mejora");
    }
    
    /**
     * Verifica si la acción es válida para la clase dada.
     */
    public boolean esValidaPara(String clase) {
        return this.claseValida.equalsIgnoreCase(clase);
    }
    
    /**
     * Convierte string a Accion (para parsing desde el lexer/parser).
     */
    public static Accion fromString(String nombre) {
        try {
            return Accion.valueOf(nombre);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
