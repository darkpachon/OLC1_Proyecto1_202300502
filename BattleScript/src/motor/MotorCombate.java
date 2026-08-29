package motor;

/**
 * Motor de cálculo de daño y efectos de combate.
 * Calcula el daño físico y mágico, aplica defensas, etc.
 */
public class MotorCombate {
    
    /**
     * Calcula el daño físico según la fórmula:
     * daño = poder_acción + ataque_físico_atacante + bonificación_WAR_CRY - armadura_defensor
     * Mínimo 1 punto de daño.
     */
    public static int calcularDanoFisico(
            int poderAccion,
            String claseAtacante,
            int bonificacionAtaque,
            String claseDefensor) {
        
        int ataqueAtacante = Estadisticas.getAtaqueFisico(claseAtacante);
        int armaduraDefensor = Estadisticas.getArmadura(claseDefensor);
        
        int dano = poderAccion + ataqueAtacante + bonificacionAtaque - armaduraDefensor;
        return Math.max(1, dano);
    }
    
    /**
     * Calcula el daño mágico según la fórmula:
     * daño = poder_acción + poder_mágico_atacante - resistencia_mágica_defensor
     * Mínimo 1 punto de daño.
     */
    public static int calcularDanoMagico(
            int poderAccion,
            String claseAtacante,
            String claseDefensor) {
        
        int poderMagicoAtacante = Estadisticas.getPoderMagico(claseAtacante);
        int resistenciaMagicaDefensor = Estadisticas.getResistenciaMagica(claseDefensor);
        
        int dano = poderAccion + poderMagicoAtacante - resistenciaMagicaDefensor;
        return Math.max(1, dano);
    }
    
    /**
     * Aplica reducción por defensa (50%).
     * Fórmula: daño defendido = floor(daño original × 0.50)
     */
    public static int aplicarReduccionDefensa(int danoOriginal) {
        return (int) Math.floor(danoOriginal * 0.50);
    }
    
    /**
     * Determina quién actúa primero según la prioridad de acciones y velocidad.
     * Retorna true si el primer combatiente actúa primero, false si el segundo.
     * 
     * @return true si atacante1 actúa primero, false si atacante2 actúa primero
     */
    public static boolean determinaPrioridad(
            Accion accion1,
            Accion accion2,
            String clase1,
            String clase2) {
        
        int prioridad1 = accion1.getPrioridad();
        int prioridad2 = accion2.getPrioridad();
        
        // Mayor prioridad actúa primero
        if (prioridad1 != prioridad2) {
            return prioridad1 > prioridad2;
        }
        
        // Si tienen la misma prioridad, actúa el de mayor velocidad
        int velocidad1 = Estadisticas.getVelocidad(clase1);
        int velocidad2 = Estadisticas.getVelocidad(clase2);
        
        if (velocidad1 != velocidad2) {
            return velocidad1 > velocidad2;
        }
        
        // Si tienen la misma velocidad, actúa el primer jugador (convención)
        return true;
    }
    
    /**
     * Calcula la vida recuperada por curación.
     * La vida no puede superar el máximo de la clase.
     * Retorna la cantidad real de vida recuperada.
     */
    public static int aplicarCuracion(
            int vidaActual,
            int vidaRecuperada,
            String clase) {
        
        int vidaMaxima = Estadisticas.getVidaMaxima(clase);
        int vidaNueva = Math.min(vidaActual + vidaRecuperada, vidaMaxima);
        return vidaNueva - vidaActual;  // Retorna lo que realmente se recuperó
    }
    
    /**
     * Calcula la recuperación de recursos (maná o energía).
     * El recurso no puede superar el máximo de la clase.
     * Retorna la cantidad real de recurso recuperado.
     */
    public static int aplicarRecuperacionRecurso(
            int recursoActual,
            int recursoRecuperado,
            String clase) {
        
        int recursoMaximo = Estadisticas.getRecursoMaximo(clase);
        int recursoNuevo = Math.min(recursoActual + recursoRecuperado, recursoMaximo);
        return recursoNuevo - recursoActual;  // Retorna lo que realmente se recuperó
    }
    
    /**
     * Verifica si una acción es de tipo defensa (reduce daño 50%).
     */
    public static boolean esAccionDefensa(Accion accion) {
        return accion.esDefensa();
    }
}
