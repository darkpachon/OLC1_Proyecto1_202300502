package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el bloque 'main' del lenguaje BattleScript en el AST.
 * Contiene todas las instrucciones de ejecución (los bloques 'run') que el 
 * compilador deberá procesar en orden.
 */
public class EjecucionMain {

    // Lista que almacena todas las instrucciones 'run' definidas en el bloque main
    private List<InstruccionRun> instrucciones;

    /**
     * Constructor principal. Inicializa la lista de instrucciones vacía.
     */
    public EjecucionMain() {
        this.instrucciones = new ArrayList<>();
    }

    public List<InstruccionRun> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(List<InstruccionRun> instrucciones) {
        this.instrucciones = instrucciones;
    }

    /**
     * Agrega una nueva instrucción 'run' a la lista de ejecución.
     * 
     * @param instruccion La instrucción a agregar.
     */
    public void agregarInstruccion(InstruccionRun instruccion) {
        this.instrucciones.add(instruccion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Main {\n");
        for (InstruccionRun inst : instrucciones) {
            sb.append("  ").append(inst.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Clase interna estática que representa una única instrucción 'run'.
     * Guarda la lista de partidas a ejecutar y la semilla aleatoria asignada.
     */
    public static class InstruccionRun {
        
        private List<String> partidas;
        private int seed;

        /**
         * Constructor de la instrucción run.
         * 
         * @param partidas Lista con los nombres de las partidas a ejecutar.
         * @param seed Semilla inicial para la generación de números pseudoaleatorios.
         */
        public InstruccionRun(List<String> partidas, int seed) {
            this.partidas = partidas;
            this.seed = seed;
        }

        public List<String> getPartidas() {
            return partidas;
        }

        public void setPartidas(List<String> partidas) {
            this.partidas = partidas;
        }

        public int getSeed() {
            return seed;
        }

        public void setSeed(int seed) {
            this.seed = seed;
        }

        @Override
        public String toString() {
            return "run " + partidas.toString() + " with { seed: " + seed + " }";
        }
    }
}