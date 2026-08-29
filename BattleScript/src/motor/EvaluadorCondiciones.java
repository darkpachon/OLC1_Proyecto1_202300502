package motor;

import ast.NodoLogico;
import ast.NodoRelacional;
import java.util.Random;

/**
 * Motor de evaluación lógica y relacional para las reglas de las estrategias.
 * Recorre recursivamente el AST (NodoLogico y NodoRelacional) para decidir acciones.
 */
public class EvaluadorCondiciones {

    private Simulador simulador;
    private EstadoCombatiente estadoActual;  // Estado del combatiente que está decidiendo
    private int jugadorActual; // 1 para Jugador 1, 2 para Jugador 2
    private Random random;

    public EvaluadorCondiciones(Simulador simulador) {
        this.simulador = simulador;
        this.random = new Random();
    }

    public void setJugadorActual(int jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public void setEstadoCombatiente(EstadoCombatiente estado) {
        this.estadoActual = estado;
    }

    /**
     * Método principal recursivo que evalúa cualquier condición (Objeto del AST o Boolean directo).
     */
    public boolean evaluar(Object condicion) {
        if (condicion == null) {
            return false;
        }

        if (condicion instanceof Boolean) {
            return (Boolean) condicion;
        }

        // 1. Evaluar Nodos Lógicos (AND, OR, NOT)
        if (condicion instanceof NodoLogico) {
            NodoLogico nodo = (NodoLogico) condicion;
            String op = nodo.getOperador();

            switch (op) {
                case "AND":
                    return evaluar(nodo.getIzquierda()) && evaluar(nodo.getDerecha());
                case "OR":
                    return evaluar(nodo.getIzquierda()) || evaluar(nodo.getDerecha());
                case "NOT":
                    return !evaluar(nodo.getIzquierda());
                default:
                    return false;
            }
        }

        // 2. Evaluar Nodos Relacionales (>, <, ==, etc.)
        if (condicion instanceof NodoRelacional) {
            NodoRelacional nodo = (NodoRelacional) condicion;
            String op = nodo.getOperador();

            // CASO ESPECIAL: Comparar RANDOM con un flotante
            if ("RANDOM".equals(nodo.getIzquierda())) {
                double valorRandom = estadoActual.getValorRandomRonda();  // Usa random de la ronda
                double valorComparacion = Double.parseDouble(nodo.getDerecha().toString());
                return compararDoubles(valorRandom, op, valorComparacion);
            }

            // CASO GENERAL: Comparar expresiones enteras
            int valIzq = evaluarEntero(nodo.getIzquierda());
            int valDer = evaluarEntero(nodo.getDerecha());

            return compararEnteros(valIzq, op, valDer);
        }

        return false;
    }

    /**
     * Resuelve variables de estado o valores fijos a números enteros.
     */
    private int evaluarEntero(Object expresion) {
        if (expresion == null) return 0;
        
        if (expresion instanceof Integer) {
            return (Integer) expresion;
        }

        if (expresion instanceof String) {
            String variable = (String) expresion;
            
            // Obtener el estado del oponente
            EstadoCombatiente estadoOponente = (jugadorActual == 1) 
                ? simulador.getEstado2() 
                : simulador.getEstado1();
            
            switch (variable) {
                case "ROUND_NUMBER":
                    return simulador.getRoundNumber();
                case "TOTAL_ROUNDS":
                    return simulador.getTotalRounds();
                case "SELF_HEALTH":
                    return estadoActual.getVida();
                case "OPPONENT_HEALTH":
                    return estadoOponente.getVida();
                case "SELF_RESOURCE":
                    return estadoActual.getRecurso();
                case "OPPONENT_RESOURCE":
                    return estadoOponente.getRecurso();
                case "SELF_SCORE":
                    return estadoActual.getPuntos();
                case "OPPONENT_SCORE":
                    return estadoOponente.getPuntos();
                default:
                    try {
                        return Integer.parseInt(variable);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
            }
        }

        return 0;
    }

    private boolean compararEnteros(int a, String operador, int b) {
        switch (operador) {
            case "IGUAL_IGUAL": return a == b;
            case "DIFERENTE":   return a != b;
            case "MAYOR":       return a > b;
            case "MENOR":       return a < b;
            case "MAYOR_IGUAL": return a >= b;
            case "MENOR_IGUAL": return a <= b;
            default:            return false;
        }
    }

    private boolean compararDoubles(double a, String operador, double b) {
        switch (operador) {
            case "IGUAL_IGUAL": return Math.abs(a - b) < 0.0001;
            case "DIFERENTE":   return Math.abs(a - b) >= 0.0001;
            case "MAYOR":       return a > b;
            case "MENOR":       return a < b;
            case "MAYOR_IGUAL": return a >= b;
            case "MENOR_IGUAL": return a <= b;
            default:            return false;
        }
    }
}