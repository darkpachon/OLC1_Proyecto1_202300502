package motor;

/**
 * Clase que representa el resultado de una ronda de combate.
 * Almacena la información sobre las acciones, daños, puntos otorgados, etc.
 */
public class ResultadoRonda {
    
    // --- INFORMACIÓN DE ACCIONES ---
    private String accionJugador1;
    private String accionJugador2;
    private Accion accionObj1;
    private Accion accionObj2;
    
    // --- INFORMACIÓN DE EJECUCIÓN ---
    private boolean puedeEjecutar1;  // Si tiene suficiente recurso
    private boolean puedeEjecutar2;
    
    // --- DAÑO CAUSADO ---
    private int danoCausadoA1;
    private int danoCausadoA2;
    private int danoOriginalA1;      // Daño antes de aplicar defensa
    private int danoOriginalA2;
    
    // --- CURACIÓN Y RECURSOS ---
    private int vidaRecuperada1;
    private int vidaRecuperada2;
    private int recursoRecuperado1;
    private int recursoRecuperado2;
    
    // --- DEFENSA ---
    private boolean jugador1Defendiendo;
    private boolean jugador2Defendiendo;
    
    // --- PUNTOS OTORGADOS ---
    private int puntosJ1;
    private int puntosJ2;
    
    // --- INFORMACIÓN DE PRIORIDAD ---
    private boolean jugador1ActuaPrimero;
    
    // --- LOG/DESCRIPCIÓN ---
    private StringBuilder descripcion;
    
    public ResultadoRonda() {
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
        this.danoCausadoA1 = 0;
        this.danoCausadoA2 = 0;
        this.danoOriginalA1 = 0;
        this.danoOriginalA2 = 0;
        this.vidaRecuperada1 = 0;
        this.vidaRecuperada2 = 0;
        this.recursoRecuperado1 = 0;
        this.recursoRecuperado2 = 0;
        this.jugador1Defendiendo = false;
        this.jugador2Defendiendo = false;
        this.puedeEjecutar1 = true;
        this.puedeEjecutar2 = true;
        this.descripcion = new StringBuilder();
    }
    
    // --- GETTERS Y SETTERS ---
    
    public String getAccionJugador1() { return accionJugador1; }
    public void setAccionJugador1(String accion) { this.accionJugador1 = accion; }
    
    public String getAccionJugador2() { return accionJugador2; }
    public void setAccionJugador2(String accion) { this.accionJugador2 = accion; }
    
    public Accion getAccionObj1() { return accionObj1; }
    public void setAccionObj1(Accion accion) { this.accionObj1 = accion; }
    
    public Accion getAccionObj2() { return accionObj2; }
    public void setAccionObj2(Accion accion) { this.accionObj2 = accion; }
    
    public boolean puedeEjecutar1() { return puedeEjecutar1; }
    public void setPuedeEjecutar1(boolean puede) { this.puedeEjecutar1 = puede; }
    
    public boolean puedeEjecutar2() { return puedeEjecutar2; }
    public void setPuedeEjecutar2(boolean puede) { this.puedeEjecutar2 = puede; }
    
    public int getDanoCausadoA1() { return danoCausadoA1; }
    public void setDanoCausadoA1(int dano) { this.danoCausadoA1 = dano; }
    
    public int getDanoCausadoA2() { return danoCausadoA2; }
    public void setDanoCausadoA2(int dano) { this.danoCausadoA2 = dano; }
    
    public int getDanoOriginalA1() { return danoOriginalA1; }
    public void setDanoOriginalA1(int dano) { this.danoOriginalA1 = dano; }
    
    public int getDanoOriginalA2() { return danoOriginalA2; }
    public void setDanoOriginalA2(int dano) { this.danoOriginalA2 = dano; }
    
    public int getVidaRecuperada1() { return vidaRecuperada1; }
    public void setVidaRecuperada1(int vida) { this.vidaRecuperada1 = vida; }
    
    public int getVidaRecuperada2() { return vidaRecuperada2; }
    public void setVidaRecuperada2(int vida) { this.vidaRecuperada2 = vida; }
    
    public int getRecursoRecuperado1() { return recursoRecuperado1; }
    public void setRecursoRecuperado1(int recurso) { this.recursoRecuperado1 = recurso; }
    
    public int getRecursoRecuperado2() { return recursoRecuperado2; }
    public void setRecursoRecuperado2(int recurso) { this.recursoRecuperado2 = recurso; }
    
    public boolean isJugador1Defendiendo() { return jugador1Defendiendo; }
    public void setJugador1Defendiendo(boolean defendiendo) { this.jugador1Defendiendo = defendiendo; }
    
    public boolean isJugador2Defendiendo() { return jugador2Defendiendo; }
    public void setJugador2Defendiendo(boolean defendiendo) { this.jugador2Defendiendo = defendiendo; }
    
    public int getPuntosJ1() { return puntosJ1; }
    public void setPuntosJ1(int puntos) { this.puntosJ1 = puntos; }
    public void agregarPuntosJ1(int puntos) { this.puntosJ1 += Math.max(0, puntos); }
    
    public int getPuntosJ2() { return puntosJ2; }
    public void setPuntosJ2(int puntos) { this.puntosJ2 = puntos; }
    public void agregarPuntosJ2(int puntos) { this.puntosJ2 += Math.max(0, puntos); }
    
    public boolean isJugador1ActuaPrimero() { return jugador1ActuaPrimero; }
    public void setJugador1ActuaPrimero(boolean actuaPrimero) { this.jugador1ActuaPrimero = actuaPrimero; }
    
    public String getDescripcion() { return descripcion.toString(); }
    public void agregarDescripcion(String texto) { 
        if (descripcion.length() > 0) {
            descripcion.append("\n");
        }
        descripcion.append(texto); 
    }
}
