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

            // CASO ESPECIAL: Comparar last_move() con una acción
            if (nodo.getIzquierda() instanceof String && 
                ((String) nodo.getIzquierda()).contains("last_move(")) {
                String ultimaAccion = evaluarUltimaAccionString(nodo.getIzquierda());
                String accionComparacion = nodo.getDerecha().toString();
                
                switch (op) {
                    case "IGUAL_IGUAL":
                        return ultimaAccion.equalsIgnoreCase(accionComparacion);
                    case "DIFERENTE":
                        return !ultimaAccion.equalsIgnoreCase(accionComparacion);
                    default:
                        return false;
                }
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
            
            // Soporte para funciones de historial: last_move(VARIABLE)
            if (variable.contains("last_move(")) {
                return evaluarUltimaAccion(variable);
            }
            
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

    /**
     * Evalúa la función last_move() que retorna la última acción del historial
     * Retorna el nombre de la acción como String envuelto en Integer (hash del nombre)
     */
    private int evaluarUltimaAccion(String funcionString) {
        try {
            // Extraer parámetro: last_move(opponent_history) → "opponent_history"
            int inicio = funcionString.indexOf("(") + 1;
            int fin = funcionString.indexOf(")");
            String parametro = funcionString.substring(inicio, fin).trim();
            
            EstadoCombatiente estado = null;
            
            // Determinar a quién pertenece el historial
            if ("OPPONENT_HISTORY".equalsIgnoreCase(parametro) || 
                "opponent_history".equalsIgnoreCase(parametro)) {
                estado = (jugadorActual == 1) ? simulador.getEstado2() : simulador.getEstado1();
            } else if ("SELF_HISTORY".equalsIgnoreCase(parametro) || 
                       "self_history".equalsIgnoreCase(parametro)) {
                estado = estadoActual;
            }
            
            if (estado != null && estado.getHistorial().size() > 0) {
                String ultimaAccion = estado.obtenerUltimAccion();
                // Retornar hash de la acción para comparación
                return ultimaAccion != null ? ultimaAccion.hashCode() : 0;
            }
        } catch (Exception e) {
            // Si hay error, retorna 0
        }
        return 0;
    }
    
    /**
     * Método auxiliar para comparar la última acción con un nombre de acción
     */
    public String evaluarUltimaAccionString(Object expresion) {
        if (expresion instanceof String) {
            String variable = (String) expresion;
            
            if (variable.contains("last_move(")) {
                try {
                    int inicio = variable.indexOf("(") + 1;
                    int fin = variable.indexOf(")");
                    String parametro = variable.substring(inicio, fin).trim();
                    
                    EstadoCombatiente estado = null;
                    
                    if ("OPPONENT_HISTORY".equalsIgnoreCase(parametro) || 
                        "opponent_history".equalsIgnoreCase(parametro)) {
                        estado = (jugadorActual == 1) ? simulador.getEstado2() : simulador.getEstado1();
                    } else if ("SELF_HISTORY".equalsIgnoreCase(parametro) || 
                               "self_history".equalsIgnoreCase(parametro)) {
                        estado = estadoActual;
                    }
                    
                    if (estado != null && estado.getHistorial().size() > 0) {
                        return estado.obtenerUltimAccion();
                    }
                } catch (Exception e) {
                    return "";
                }
            }
        }
        return "";
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