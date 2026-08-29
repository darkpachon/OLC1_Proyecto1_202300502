package motor;

import ast.Partida;
import java.util.List;

/**
 * Gestor del sistema de puntuación y bonificaciones.
 * Se encarga de calcular puntos por daño, curación, defensa, combos, etc.
 */
public class GestorPuntuacion {
    
    private Partida partida;
    
    public GestorPuntuacion(Partida partida) {
        this.partida = partida;
    }
    
    /**
     * Calcula puntos por daño realizado.
     * Fórmula: daño real × damage_point
     */
    public int calcularPuntosPorDano(int danoReal) {
        int damagePoint = partida.getDamagePoint() > 0 ? partida.getDamagePoint() : 1;
        return Math.max(0, danoReal * damagePoint);
    }
    
    /**
     * Calcula puntos por curación realizada.
     * Fórmula: vida recuperada × healing_point
     */
    public int calcularPuntosPorCuracion(int vidaRecuperada) {
        int healingPoint = partida.getHealingPoint() > 0 ? partida.getHealingPoint() : 1;
        return Math.max(0, vidaRecuperada * healingPoint);
    }
    
    /**
     * Otorga puntos por defensa exitosa.
     * Se otorgan si la defensa redujo al menos 1 punto de daño.
     */
    public int obtenerPuntosPorDefensa() {
        return partida.getSuccessfulDefense() > 0 ? partida.getSuccessfulDefense() : 20;
    }
    
    /**
     * Otorga bonificación por victoria.
     */
    public int obtenerBonusVictoria() {
        return partida.getVictoryBonus() > 0 ? partida.getVictoryBonus() : 100;
    }
    
    /**
     * Otorga penalización por acción fallida (sin recursos suficientes).
     */
    public int obtenerPenalizacionAccionFallida() {
        return partida.getFailedActionPenalty() > 0 ? partida.getFailedActionPenalty() : 10;
    }
    
    /**
     * Verifica si se activó el combo del mago.
     * Retorna los puntos del combo o 0 si no se activó.
     */
    public int verificarComboMago(List<String> ultimas3Acciones) {
        List<String> comboEsperado = partida.getMageCombo();
        
        if (comboEsperado == null || comboEsperado.size() < 3) {
            return 0;
        }
        
        // Verificar si las últimas 3 acciones coinciden con el combo
        if (ultimas3Acciones.size() >= 3) {
            String acc1 = ultimas3Acciones.get(ultimas3Acciones.size() - 3);
            String acc2 = ultimas3Acciones.get(ultimas3Acciones.size() - 2);
            String acc3 = ultimas3Acciones.get(ultimas3Acciones.size() - 1);
            
            if (acc1.equals(comboEsperado.get(0)) &&
                acc2.equals(comboEsperado.get(1)) &&
                acc3.equals(comboEsperado.get(2))) {
                return partida.getMageComboPoints() > 0 ? partida.getMageComboPoints() : 35;
            }
        }
        
        return 0;
    }
    
    /**
     * Verifica si se activó el combo del guerrero.
     * Retorna los puntos del combo o 0 si no se activó.
     */
    public int verificarComboGuerrero(List<String> ultimas3Acciones) {
        List<String> comboEsperado = partida.getWarriorCombo();
        
        if (comboEsperado == null || comboEsperado.size() < 3) {
            return 0;
        }
        
        // Verificar si las últimas 3 acciones coinciden con el combo
        if (ultimas3Acciones.size() >= 3) {
            String acc1 = ultimas3Acciones.get(ultimas3Acciones.size() - 3);
            String acc2 = ultimas3Acciones.get(ultimas3Acciones.size() - 2);
            String acc3 = ultimas3Acciones.get(ultimas3Acciones.size() - 1);
            
            if (acc1.equals(comboEsperado.get(0)) &&
                acc2.equals(comboEsperado.get(1)) &&
                acc3.equals(comboEsperado.get(2))) {
                return partida.getWarriorComboPoints() > 0 ? partida.getWarriorComboPoints() : 40;
            }
        }
        
        return 0;
    }
    
    /**
     * Verifica si hay bonus por victoria con poca vida.
     * Se otorga cuando el ganador termina con vida <= 25% de su vida máxima.
     */
    public int verificarBonusVictoriaConPocaVida(String claseGanador, int vidaGanador) {
        int vidaMaxima = Estadisticas.getVidaMaxima(claseGanador);
        int porcentaje25 = (int) (vidaMaxima * 0.25);
        
        if (vidaGanador <= porcentaje25) {
            return partida.getLowHealthVictory() > 0 ? partida.getLowHealthVictory() : 25;
        }
        
        return 0;
    }
    
    /**
     * Aplica penalización por acción fallida.
     * La puntuación no puede quedar por debajo de cero.
     */
    public int aplicarPenalizacion(int puntuacionActual, int penalizacion) {
        return Math.max(0, puntuacionActual - penalizacion);
    }
}
