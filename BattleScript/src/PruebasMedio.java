import ast.*;
import java.util.ArrayList;
import java.util.List;
import motor.*;

/**
 * Programa que ejecuta las estrategias del archivo Medio.btl
 * Aurora (Mago) vs Titan (Guerrero)
 * Luna (Mago) vs Leon (Guerrero)
 */
public class PruebasMedio {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("PRUEBA DE BATTLESCRIPT - ARCHIVO MEDIO.BTL");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Crear Partida Uno: Aurora vs Titan
        Partida partidaUno = crearPartidaUno();
        Estrategia aurora = crearEstrategiaAurora();
        Estrategia titan = crearEstrategiaTitan();
        
        // Crear Partida Dos: Luna vs Leon
        Partida partidaDos = crearPartidaDos();
        Estrategia luna = crearEstrategiaLuna();
        Estrategia leon = crearEstrategiaLeon();
        
        // Ejecutar Partida Uno
        System.out.println("\n>>> PARTIDA UNO: Aurora (Mago) vs Titan (Guerrero)");
        Simulador simulador1 = new Simulador(partidaUno, aurora, titan, 55);
        String resultado1 = simulador1.ejecutarPartida();
        System.out.println(resultado1);
        
        // Ejecutar Partida Dos
        System.out.println("\n>>> PARTIDA DOS: Luna (Mago) vs Leon (Guerrero)");
        Simulador simulador2 = new Simulador(partidaDos, luna, leon, 55);
        String resultado2 = simulador2.ejecutarPartida();
        System.out.println(resultado2);
        
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("✅ PRUEBAS COMPLETADAS");
        System.out.println("=".repeat(70));
    }
    
    // ============= ESTRATEGIA 1: AURORA (MAGO) =============
    private static Estrategia crearEstrategiaAurora() {
        Estrategia aurora = new Estrategia("Aurora", "mage");
        aurora.setAccionInicial("ARCANE_BOLT");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: if self_health < 40 then HEALING_RUNE
        NodoRelacional cond1 = new NodoRelacional("SELF_HEALTH", "MENOR", 40);
        reglas.add(new Regla(cond1, "HEALING_RUNE"));
        
        // Regla 2: if self_resource > 50 && opponent_health < 60 then FIREBALL
        NodoRelacional cond2a = new NodoRelacional("SELF_RESOURCE", "MAYOR", 50);
        NodoRelacional cond2b = new NodoRelacional("OPPONENT_HEALTH", "MENOR", 60);
        NodoLogico cond2 = new NodoLogico(cond2a, "AND", cond2b);
        reglas.add(new Regla(cond2, "FIREBALL"));
        
        // Regla 3: if last_move(opponent_history) == HEAVY_STRIKE then MAGIC_BARRIER
        NodoRelacional cond3 = new NodoRelacional("last_move(opponent_history)", "IGUAL_IGUAL", "HEAVY_STRIKE");
        reglas.add(new Regla(cond3, "MAGIC_BARRIER"));
        
        aurora.setReglas(reglas);
        aurora.setAccionPorDefecto("ARCANE_BOLT");
        
        return aurora;
    }
    
    // ============= ESTRATEGIA 2: TITAN (GUERRERO) =============
    private static Estrategia crearEstrategiaTitan() {
        Estrategia titan = new Estrategia("Titan", "warrior");
        titan.setAccionInicial("SLASH");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: if self_health < 35 then SHIELD_BLOCK
        NodoRelacional cond1 = new NodoRelacional("SELF_HEALTH", "MENOR", 35);
        reglas.add(new Regla(cond1, "SHIELD_BLOCK"));
        
        // Regla 2: if self_resource > 40 || opponent_health < 30 then HEAVY_STRIKE
        NodoRelacional cond2a = new NodoRelacional("SELF_RESOURCE", "MAYOR", 40);
        NodoRelacional cond2b = new NodoRelacional("OPPONENT_HEALTH", "MENOR", 30);
        NodoLogico cond2 = new NodoLogico(cond2a, "OR", cond2b);
        reglas.add(new Regla(cond2, "HEAVY_STRIKE"));
        
        // Regla 3: if last_move(opponent_history) == FIREBALL then SHIELD_BLOCK
        NodoRelacional cond3 = new NodoRelacional("last_move(opponent_history)", "IGUAL_IGUAL", "FIREBALL");
        reglas.add(new Regla(cond3, "SHIELD_BLOCK"));
        
        titan.setReglas(reglas);
        titan.setAccionPorDefecto("SLASH");
        
        return titan;
    }
    
    // ============= ESTRATEGIA 3: LUNA (MAGO) =============
    private static Estrategia crearEstrategiaLuna() {
        Estrategia luna = new Estrategia("Luna", "mage");
        luna.setAccionInicial("MAGIC_BARRIER");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: if self_resource < 25 then MEDITATE
        NodoRelacional cond1 = new NodoRelacional("SELF_RESOURCE", "MENOR", 25);
        reglas.add(new Regla(cond1, "MEDITATE"));
        
        // Regla 2: if opponent_health < 50 && self_resource > 30 then FIREBALL
        NodoRelacional cond2a = new NodoRelacional("OPPONENT_HEALTH", "MENOR", 50);
        NodoRelacional cond2b = new NodoRelacional("SELF_RESOURCE", "MAYOR", 30);
        NodoLogico cond2 = new NodoLogico(cond2a, "AND", cond2b);
        reglas.add(new Regla(cond2, "FIREBALL"));
        
        luna.setReglas(reglas);
        luna.setAccionPorDefecto("ARCANE_BOLT");
        
        return luna;
    }
    
    // ============= ESTRATEGIA 4: LEON (GUERRERO) =============
    private static Estrategia crearEstrategiaLeon() {
        Estrategia leon = new Estrategia("Leon", "warrior");
        leon.setAccionInicial("WAR_CRY");
        
        List<Regla> reglas = new ArrayList<>();
        
        // Regla 1: if self_health < 45 then SHIELD_BLOCK
        NodoRelacional cond1 = new NodoRelacional("SELF_HEALTH", "MENOR", 45);
        reglas.add(new Regla(cond1, "SHIELD_BLOCK"));
        
        // Regla 2: if opponent_health < 40 || self_resource > 60 then HEAVY_STRIKE
        NodoRelacional cond2a = new NodoRelacional("OPPONENT_HEALTH", "MENOR", 40);
        NodoRelacional cond2b = new NodoRelacional("SELF_RESOURCE", "MAYOR", 60);
        NodoLogico cond2 = new NodoLogico(cond2a, "OR", cond2b);
        reglas.add(new Regla(cond2, "HEAVY_STRIKE"));
        
        leon.setReglas(reglas);
        leon.setAccionPorDefecto("SLASH");
        
        return leon;
    }
    
    // ============= PARTIDA 1 =============
    private static Partida crearPartidaUno() {
        Partida partida = new Partida("PartidaUno");
        
        List<String> jugadores = new ArrayList<>();
        jugadores.add("Aurora");
        jugadores.add("Titan");
        partida.setJugadores(jugadores);
        
        partida.setRondas(6);
        
        // Configuración de puntuación
        partida.setDamagePoint(1);
        partida.setHealingPoint(1);
        partida.setSuccessfulDefense(20);
        partida.setVictoryBonus(100);
        partida.setFailedActionPenalty(10);
        
        // Configuración de bonos
        List<String> mageCombo = new ArrayList<>();
        mageCombo.add("ARCANE_BOLT");
        mageCombo.add("ARCANE_BOLT");
        mageCombo.add("FIREBALL");
        partida.setMageCombo(mageCombo);
        partida.setMageComboPoints(30);
        
        List<String> warriorCombo = new ArrayList<>();
        warriorCombo.add("SLASH");
        warriorCombo.add("SLASH");
        warriorCombo.add("HEAVY_STRIKE");
        partida.setWarriorCombo(warriorCombo);
        partida.setWarriorComboPoints(35);
        
        partida.setLowHealthVictory(20);
        
        return partida;
    }
    
    // ============= PARTIDA 2 =============
    private static Partida crearPartidaDos() {
        Partida partida = new Partida("PartidaDos");
        
        List<String> jugadores = new ArrayList<>();
        jugadores.add("Luna");
        jugadores.add("Leon");
        partida.setJugadores(jugadores);
        
        partida.setRondas(6);
        
        // Configuración de puntuación
        partida.setDamagePoint(1);
        partida.setHealingPoint(1);
        partida.setSuccessfulDefense(20);
        partida.setVictoryBonus(100);
        partida.setFailedActionPenalty(10);
        
        // Configuración de bonos
        List<String> mageCombo = new ArrayList<>();
        mageCombo.add("ARCANE_BOLT");
        mageCombo.add("ARCANE_BOLT");
        mageCombo.add("FIREBALL");
        partida.setMageCombo(mageCombo);
        partida.setMageComboPoints(30);
        
        List<String> warriorCombo = new ArrayList<>();
        warriorCombo.add("SLASH");
        warriorCombo.add("SLASH");
        warriorCombo.add("HEAVY_STRIKE");
        partida.setWarriorCombo(warriorCombo);
        partida.setWarriorComboPoints(35);
        
        partida.setLowHealthVictory(20);
        
        return partida;
    }
}
