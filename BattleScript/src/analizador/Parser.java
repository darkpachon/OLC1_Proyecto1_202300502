package analizador;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Parser generada a partir de Parser.cup.
 * TODO: Generar automáticamente con CUP.
 */
public class Parser {
    
    private Lexer lexer;
    public List<String> erroresSintacticos;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.erroresSintacticos = new ArrayList<>();
    }
    
    public Object parse() throws Exception {
        // Placeholder: Implementar con CUP
        return null;
    }
}
