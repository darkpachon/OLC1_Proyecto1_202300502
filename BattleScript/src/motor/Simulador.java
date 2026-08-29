package motor;

import ast.Estrategia;
import ast.Partida;
import ast.Regla;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Motor principal de BattleScript.
 * Ejecuta la simulación ronda por ronda utilizando las reglas, el AST y la semilla.
 */
public class Simulador {
    
    private Partida partida;
    private Estrategia estrategia1;
    private Estrategia estrategia2;
    private int seed;
    
    // --- Estados de los combatientes ---
    private EstadoCombatiente estado1;
    private EstadoCombatiente estado2;
    
    // --- Generadores pseudoaleatorios independientes ---
    private Random random1;
    private Random random2;
    
    // --- Evaluador de condiciones y gestores ---
    private EvaluadorCondiciones evaluador;
    private GestorPuntuacion gestorPuntuacion;
    private int roundNumber = 0;

    public Simulador(Partida partida, Estrategia jugador1, Estrategia jugador2, int seed) {
        this.partida = partida;
        this.estrategia1 = jugador1;
        this.estrategia2 = jugador2;
        this.seed = seed;
        
        // Inicializar estados de los combatientes
        this.estado1 = new EstadoCombatiente(jugador1);
        this.estado2 = new EstadoCombatiente(jugador2);
        
        // Inicializar generadores pseudoaleatorios independientes
        // Jugador 1 usa seed, Jugador 2 usa seed + 1
        this.random1 = new Random(seed);
        this.random2 = new Random(seed + 1);
        
        // Evaluador y gestor de puntuación
        this.evaluador = new EvaluadorCondiciones(this);
        this.gestorPuntuacion = new GestorPuntuacion(partida);
    }
    
    /**
     * Ejecuta el combate completo de principio a fin.
     */
    public String ejecutarPartida() {
        StringBuilder log = new StringBuilder();
        log.append("=================================================\n");
        log.append("=== INICIANDO PARTIDA: ").append(partida.getNombre() != null ? partida.getNombre() : "Duelo").append(" ===\n");
        log.append("Semilla (Seed): ").append(seed).append("\n");
        log.append("Jugador 1: ").append(estrategia1.getNombre()).append(" (").append(estrategia1.getClaseEstrategia()).append(")\n");
        log.append("Jugador 2: ").append(estrategia2.getNombre()).append(" (").append(estrategia2.getClaseEstrategia()).append(")\n");
        log.append("Rondas totales: ").append(partida.getRondas()).append("\n");
        log.append("=================================================\n\n");

        int totalRondas = partida.getRondas();
        
        // --- RONDA 0: Acción inicial (sin evaluación de reglas) ---
        log.append("--- RONDA 0 (Acción Inicial) ---\n");
        String accionInicial1 = estrategia1.getAccionInicial();
        String accionInicial2 = estrategia2.getAccionInicial();
        
        log.append(estrategia1.getNombre()).append(": ").append(accionInicial1).append("\n");
        log.append(estrategia2.getNombre()).append(": ").append(accionInicial2).append("\n");
        
        // Ejecutar acciones iniciales
        ResultadoRonda resultado0 = new ResultadoRonda();
        resultado0.setAccionJugador1(accionInicial1);
        resultado0.setAccionJugador2(accionInicial2);
        resolverRonda(resultado0, log);
        
        estado1.agregarAlHistorial(accionInicial1);
        estado2.agregarAlHistorial(accionInicial2);
        
        log.append("  [ESTADO] ").append(estrategia1.getNombre()).append(": ").append(estado1.getVida())
           .append(" HP / ").append(estado1.getRecurso()).append(" Recurso  |  ")
           .append(estrategia2.getNombre()).append(": ").append(estado2.getVida()).append(" HP / ")
           .append(estado2.getRecurso()).append(" Recurso\n\n");

        // Verificar si alguien ya fue derrotado en ronda 0
        if (!estado1.estaVivo() || !estado2.estaVivo()) {
            determinarGanador(log);
            return log.toString();
        }

        // --- RONDAS 1 a N: Evaluación de reglas ---
        for (roundNumber = 1; roundNumber < totalRondas; roundNumber++) {
            log.append("--- RONDA ").append(roundNumber).append(" ---\n");
            
            // Generar valores random independientes para cada jugador
            estado1.setValorRandomRonda(random1.nextDouble());
            estado2.setValorRandomRonda(random2.nextDouble());
            
            // Resetear estados temporales
            estado1.resetearEstadosTemporales();
            estado2.resetearEstadosTemporales();

            // 1. Decidir acciones evaluando reglas
            String accion1 = decidirAccion(estrategia1, estado1, 1);
            String accion2 = decidirAccion(estrategia2, estado2, 2);

            log.append(estrategia1.getNombre()).append(": ").append(accion1).append("\n");
            log.append(estrategia2.getNombre()).append(": ").append(accion2).append("\n");

            // 2. Resolver acciones
            ResultadoRonda resultado = new ResultadoRonda();
            resultado.setAccionJugador1(accion1);
            resultado.setAccionJugador2(accion2);
            resolverRonda(resultado, log);

            // 3. Agregar al historial solo si se ejecutaron
            if (resultado.puedeEjecutar1()) {
                estado1.agregarAlHistorial(accion1);
            }
            if (resultado.puedeEjecutar2()) {
                estado2.agregarAlHistorial(accion2);
            }

            // 4. Mostrar estado actual
            log.append("  [ESTADO] ").append(estrategia1.getNombre()).append(": ").append(estado1.getVida())
               .append(" HP / ").append(estado1.getRecurso()).append(" Recurso | Puntos: ").append(estado1.getPuntos())
               .append("  |  ").append(estrategia2.getNombre()).append(": ").append(estado2.getVida()).append(" HP / ")
               .append(estado2.getRecurso()).append(" Recurso | Puntos: ").append(estado2.getPuntos()).append("\n\n");

            // 5. Verificar si la partida terminó por muerte
            if (!estado1.estaVivo() || !estado2.estaVivo()) {
                log.append("¡Uno de los combatientes ha sido derrotado!\n\n");
                break;
            }
        }

        determinarGanador(log);
        return log.toString();
    }

    /**
     * Evalúa las reglas (if-then-else) de una estrategia para elegir una acción.
     */
    private String decidirAccion(Estrategia estrategia, EstadoCombatiente estado, int idJugador) {
        evaluador.setJugadorActual(idJugador);
        evaluador.setEstadoCombatiente(estado);
        
        List<Regla> reglas = estrategia.getReglas();
        
        if (reglas != null) {
            for (Regla regla : reglas) {
                // Evaluar la condición del nodo usando el evaluador
                boolean seCumple = evaluador.evaluar(regla.getCondicion());
                if (seCumple) {
                    return regla.getAccion();  // Se ejecuta el THEN
                }
            }
        }
        
        // Si ninguna regla se cumple, se ejecuta la acción por defecto (else)
        return estrategia.getAccionPorDefecto();
    }

    /**
     * Resuelve una ronda completa: determina prioridad, ejecuta acciones, calcula daño, puntos, etc.
     */
    private void resolverRonda(ResultadoRonda resultado, StringBuilder log) {
        String nombreAccion1 = resultado.getAccionJugador1();
        String nombreAccion2 = resultado.getAccionJugador2();
        
        // Obtener objetos Accion
        Accion accion1 = Accion.fromString(nombreAccion1);
        Accion accion2 = Accion.fromString(nombreAccion2);
        
        resultado.setAccionObj1(accion1);
        resultado.setAccionObj2(accion2);
        
        // Verificar si pueden ejecutar (tienen suficiente recurso)
        boolean puede1 = accion1 == null || estado1.puedeEjecutar(accion1);
        boolean puede2 = accion2 == null || estado2.puedeEjecutar(accion2);
        
        resultado.setPuedeEjecutar1(puede1);
        resultado.setPuedeEjecutar2(puede2);
        
        // Si no puede ejecutar, aplicar penalización
        if (!puede1) {
            int penalizacion = gestorPuntuacion.obtenerPenalizacionAccionFallida();
            estado1.setPuntos(gestorPuntuacion.aplicarPenalizacion(estado1.getPuntos(), penalizacion));
            log.append("  ⚠️ ").append(estrategia1.getNombre()).append(" intentó ").append(nombreAccion1)
               .append(" pero no tiene suficiente recurso. Penalización: ").append(penalizacion).append(" puntos.\n");
            accion1 = null;
        }
        if (!puede2) {
            int penalizacion = gestorPuntuacion.obtenerPenalizacionAccionFallida();
            estado2.setPuntos(gestorPuntuacion.aplicarPenalizacion(estado2.getPuntos(), penalizacion));
            log.append("  ⚠️ ").append(estrategia2.getNombre()).append(" intentó ").append(nombreAccion2)
               .append(" pero no tiene suficiente recurso. Penalización: ").append(penalizacion).append(" puntos.\n");
            accion2 = null;
        }
        
        // Si ambas acciones son nulas, fin de la ronda
        if (accion1 == null && accion2 == null) {
            return;
        }
        
        // Determinar prioridad de ejecución
        boolean jugador1Primero;
        if (accion1 != null && accion2 != null) {
            jugador1Primero = MotorCombate.determinaPrioridad(
                accion1, accion2,
                estrategia1.getClaseEstrategia(),
                estrategia2.getClaseEstrategia()
            );
        } else {
            jugador1Primero = accion1 != null;  // El que tenga acción válida actúa
        }
        
        resultado.setJugador1ActuaPrimero(jugador1Primero);
        
        // Ejecutar acciones en orden de prioridad
        if (jugador1Primero) {
            if (accion1 != null) ejecutarAccion(1, accion1, estado1, resultado, log);
            if (accion2 != null) ejecutarAccion(2, accion2, estado2, resultado, log);
        } else {
            if (accion2 != null) ejecutarAccion(2, accion2, estado2, resultado, log);
            if (accion1 != null) ejecutarAccion(1, accion1, estado1, resultado, log);
        }
        
        // Consumir costos de recursos
        if (puede1 && accion1 != null) {
            estado1.setRecurso(estado1.getRecurso() - accion1.getCosto());
        }
        if (puede2 && accion2 != null) {
            estado2.setRecurso(estado2.getRecurso() - accion2.getCosto());
        }
    }

    /**
     * Ejecuta una acción específica y resuelve sus efectos.
     */
    private void ejecutarAccion(int idJugador, Accion accion, EstadoCombatiente estado, ResultadoRonda resultado, StringBuilder log) {
        EstadoCombatiente atacante = estado;
        EstadoCombatiente defensor = (idJugador == 1) ? estado2 : estado1;
        Estrategia estrategiaAtacante = (idJugador == 1) ? estrategia1 : estrategia2;
        Estrategia estrategiaDefensor = (idJugador == 1) ? estrategia2 : estrategia1;
        
        String nombreAtacante = estrategiaAtacante.getNombre();
        String nombreDefensor = estrategiaDefensor.getNombre();
        
        if (accion.esAtaque()) {
            int danoBase;
            if (accion.getTipo().contains("físico")) {
                danoBase = MotorCombate.calcularDanoFisico(
                    accion.getPoder(),
                    estrategiaAtacante.getClaseEstrategia(),
                    atacante.getBonificacionProximoAtaque(),
                    estrategiaDefensor.getClaseEstrategia()
                );
            } else {
                danoBase = MotorCombate.calcularDanoMagico(
                    accion.getPoder(),
                    estrategiaAtacante.getClaseEstrategia(),
                    estrategiaDefensor.getClaseEstrategia()
                );
            }
            
            int danoFinal = danoBase;
            if (defensor.estaDefiendose()) {
                danoFinal = MotorCombate.aplicarReduccionDefensa(danoBase);
                log.append("  🛡️ ").append(nombreDefensor).append(" reduce el daño de ").append(danoBase).append(" a ").append(danoFinal).append(".\n");
            }
            
            defensor.setVida(defensor.getVida() - danoFinal);
            
            if (idJugador == 1) {
                resultado.setDanoOriginalA1(danoBase);
                resultado.setDanoCausadoA1(danoFinal);
                resultado.agregarPuntosJ1(gestorPuntuacion.calcularPuntosPorDano(danoFinal));
            } else {
                resultado.setDanoOriginalA2(danoBase);
                resultado.setDanoCausadoA2(danoFinal);
                resultado.agregarPuntosJ2(gestorPuntuacion.calcularPuntosPorDano(danoFinal));
            }
            
            log.append("  ⚔️ ").append(nombreAtacante).append(" usa ").append(accion.getNombre()).append(" (daño: ").append(danoFinal).append(").\n");
            atacante.consumirBonificacionAtaque();
            
        } else if (accion.esCuracion()) {
            int vidaRecuperada = MotorCombate.aplicarCuracion(
                atacante.getVida(),
                accion.getPoder(),
                estrategiaAtacante.getClaseEstrategia()
            );
            
            atacante.setVida(atacante.getVida() + vidaRecuperada);
            
            if (idJugador == 1) {
                resultado.setVidaRecuperada1(vidaRecuperada);
                resultado.agregarPuntosJ1(gestorPuntuacion.calcularPuntosPorCuracion(vidaRecuperada));
            } else {
                resultado.setVidaRecuperada2(vidaRecuperada);
                resultado.agregarPuntosJ2(gestorPuntuacion.calcularPuntosPorCuracion(vidaRecuperada));
            }
            
            log.append("  💚 ").append(nombreAtacante).append(" usa ").append(accion.getNombre()).append(" (vida recuperada: ").append(vidaRecuperada).append(").\n");
            
        } else if (accion.esDefensa()) {
            atacante.setDefiendose(true);
            if (idJugador == 1) {
                resultado.setJugador1Defendiendo(true);
            } else {
                resultado.setJugador2Defendiendo(true);
            }
            
            log.append("  🛡️ ").append(nombreAtacante).append(" se defiende con ").append(accion.getNombre()).append(".\n");
            
        } else if (accion.esRecuperacion()) {
            int recursoRecuperado = MotorCombate.aplicarRecuperacionRecurso(
                atacante.getRecurso(),
                accion.getPoder(),
                estrategiaAtacante.getClaseEstrategia()
            );
            
            atacante.setRecurso(atacante.getRecurso() + recursoRecuperado);
            
            if (idJugador == 1) {
                resultado.setRecursoRecuperado1(recursoRecuperado);
            } else {
                resultado.setRecursoRecuperado2(recursoRecuperado);
            }
            
            log.append("  ⚡ ").append(nombreAtacante).append(" usa ").append(accion.getNombre()).append(" (recurso recuperado: ").append(recursoRecuperado).append(").\n");
            
        } else if (accion.esMejora()) {
            atacante.setBonificacionProximoAtaque(accion.getPoder());
            log.append("  💪 ").append(nombreAtacante).append(" usa ").append(accion.getNombre()).append(" (+").append(accion.getPoder()).append(" al próximo ataque).\n");
        }
        
        estado1.agregarPuntos(resultado.getPuntosJ1());
        estado2.agregarPuntos(resultado.getPuntosJ2());
    }

    /**
     * Determina el ganador de la partida.
     */
    private void determinarGanador(StringBuilder log) {
        log.append("=================================================\n");
        log.append("=== FIN DE LA PARTIDA ===\n\n");
        
        int vida1 = estado1.getVida();
        int vida2 = estado2.getVida();
        int puntos1 = estado1.getPuntos();
        int puntos2 = estado2.getPuntos();
        int recurso1 = estado1.getRecurso();
        int recurso2 = estado2.getRecurso();
        
        log.append("RESULTADOS FINALES:\n");
        log.append("  ").append(estrategia1.getNombre()).append(": ").append(vida1).append(" HP | ")
           .append(puntos1).append(" Puntos | ").append(recurso1).append(" Recurso\n");
        log.append("  ").append(estrategia2.getNombre()).append(": ").append(vida2).append(" HP | ")
           .append(puntos2).append(" Puntos | ").append(recurso2).append(" Recurso\n\n");
        
        Estrategia ganador = null;
        String razonVictoria = "";
        
        if (vida1 <= 0 && vida2 > 0) {
            ganador = estrategia2;
            razonVictoria = "Derrota del oponente";
        } else if (vida2 <= 0 && vida1 > 0) {
            ganador = estrategia1;
            razonVictoria = "Derrota del oponente";
        }
        else if (puntos1 > puntos2) {
            ganador = estrategia1;
            razonVictoria = "Mayor puntuación";
        } else if (puntos2 > puntos1) {
            ganador = estrategia2;
            razonVictoria = "Mayor puntuación";
        }
        else if (vida1 > vida2) {
            ganador = estrategia1;
            razonVictoria = "Mayor vida restante";
        } else if (vida2 > vida1) {
            ganador = estrategia2;
            razonVictoria = "Mayor vida restante";
        }
        else if (recurso1 > recurso2) {
            ganador = estrategia1;
            razonVictoria = "Mayor recurso restante";
        } else if (recurso2 > recurso1) {
            ganador = estrategia2;
            razonVictoria = "Mayor recurso restante";
        }
        
        if (ganador != null) {
            log.append("🏆 ¡GANADOR: ").append(ganador.getNombre()).append("!\n");
            log.append("   Razón: ").append(razonVictoria).append("\n");
            
            EstadoCombatiente estadoGanador = ganador == estrategia1 ? estado1 : estado2;
            int bonusVictoria = gestorPuntuacion.obtenerBonusVictoria();
            estadoGanador.agregarPuntos(bonusVictoria);
            
            int bonusVidaBaja = gestorPuntuacion.verificarBonusVictoriaConPocaVida(
                ganador.getClaseEstrategia(),
                estadoGanador.getVida()
            );
            if (bonusVidaBaja > 0) {
                estadoGanador.agregarPuntos(bonusVidaBaja);
                log.append("   Bonus por victoria con poca vida: +").append(bonusVidaBaja).append("\n");
            }
        } else {
            log.append("⚖️ ¡EMPATE!\n");
        }
        
        log.append("=================================================\n");
    }

    // --- GETTERS ---
    
    public int getRoundNumber() { return roundNumber; }
    public int getTotalRounds() { return partida.getRondas(); }
    
    public EstadoCombatiente getEstado1() { return estado1; }
    public EstadoCombatiente getEstado2() { return estado2; }
    
    public Estrategia getEstrategia1() { return estrategia1; }
    public Estrategia getEstrategia2() { return estrategia2; }

    public Random getRandom1() { return random1; }
    public Random getRandom2() { return random2; }
}