import ast.*;
import java.util.ArrayList;
import java.util.List;
import motor.*;

/**
 * Programa principal de BattleScript.
 * Permite ejecutar simulaciones de duelos entre estrategias.
 */
public class Main {
    
    public static void main(String[] args) {
        // Crear estrategias de prueba
        Estrategia merlin = crearEstrategiaMago();
        Estrategia ragnar = crearEstrategiaGuerrero();
        
        // Crear partida
        Partida partida = crearPartida();
        
        // Ejecutar simulación con seed 42
        Simulador sim = new Simulador(partida, merlin, ragnar, 42);
        String resultado = sim.ejecutarPartida();
        
        // Mostrar resultado
        System.out.println(resultado);
    }
    
    private static Estrategia crearEstrategiaMago() {
        Estrategia mago = new Estrategia("Merlin", "mage");
        mago.setAccionInicial("ARCANE_BOLT");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: Si vida baja, curar
        NodoRelacional cond1 = new NodoRelacional("SELF_HEALTH", "MENOR", 30);
        reglas.add(new Regla(cond1, "HEALING_RUNE"));
        
        // Regla 2: Si oponente tiene mucha vida, ataque fuerte
        NodoRelacional cond2 = new NodoRelacional("OPPONENT_HEALTH", "MAYOR", 50);
        reglas.add(new Regla(cond2, "FIREBALL"));
        
        // Regla 3: Si es ronda avanzada, meditar
        NodoRelacional cond3 = new NodoRelacional("ROUND_NUMBER", "MAYOR", 5);
        reglas.add(new Regla(cond3, "MEDITATE"));
        
        mago.setReglas(reglas);
        mago.setAccionPorDefecto("ARCANE_BOLT");
        
        return mago;
    }
    
    private static Estrategia crearEstrategiaGuerrero() {
        Estrategia guerrero = new Estrategia("Ragnar", "warrior");
        guerrero.setAccionInicial("SLASH");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: Si vida crítica, descansar
        NodoRelacional cond1 = new NodoRelacional("SELF_HEALTH", "MENOR", 25);
        reglas.add(new Regla(cond1, "REST"));
        
        // Regla 2: Si oponente con mucha vida, ataque pesado
        NodoRelacional cond2 = new NodoRelacional("OPPONENT_HEALTH", "MAYOR", 60);
        reglas.add(new Regla(cond2, "HEAVY_STRIKE"));
        
        // Regla 3: Si es ronda 3+, grito de guerra
        NodoRelacional cond3 = new NodoRelacional("ROUND_NUMBER", "MAYOR", 3);
        reglas.add(new Regla(cond3, "WAR_CRY"));
        
        guerrero.setReglas(reglas);
        guerrero.setAccionPorDefecto("SLASH");
        
        return guerrero;
    }
    
    private static Partida crearPartida() {
        Partida partida = new Partida("DueloPrueba");
        partida.setRondas(10);
        partida.setDamagePoint(2);
        partida.setHealingPoint(3);
        partida.setSuccessfulDefense(5);
        partida.setVictoryBonus(20);
        partida.setFailedActionPenalty(3);
        return partida;
    }
}
