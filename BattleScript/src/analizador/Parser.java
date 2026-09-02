package analizador;

import java.util.ArrayList;
import java.util.List;

import ast.*;
import reportes.ErrorLexicoSintactico;

/**
 * Parser generado a partir de Parser.cup.
 * Este es un stub funcional que permite la compilación.
 * Para funcionalidad completa, regenerar con: java -cp lib/java-cup-11b.jar java_cup.Main -destdir src/analizador src/analizador/Parser.cup
 */
public class Parser {
    
    private Lexer lexer;
    public ArrayList<ErrorLexicoSintactico> erroresSintacticos;
    private ArrayList<Estrategia> estrategias;
    private ArrayList<Partida> partidas;
    private Object mainEjecucion;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.erroresSintacticos = new ArrayList<>();
        this.estrategias = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }
    
    /**
     * Parsea el contenido del Lexer y construye el AST.
     * Por ahora es un stub; implementar completamente cuando CUP funcione.
     */
    public Object parse() throws Exception {
        // TODO: Implementar parser completo con CUP
        // Por ahora, retorna null (estructura AST vacía)
        return null;
    }
    
    public ArrayList<Estrategia> getEstrategias() { return estrategias; }
    public ArrayList<Partida> getPartidas() { return partidas; }
    public Object getMainEjecucion() { return mainEjecucion; }
}
