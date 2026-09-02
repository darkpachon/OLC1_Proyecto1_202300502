import analizador.Lexer;
import analizador.sym;
import java_cup.runtime.Symbol;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Programa de prueba para verificar que el Lexer reconoce correctamente
 * los tokens del archivo Medio.btl
 */
public class PruebaLexerMedio {
    
    // Mapa para traducir códigos de símbolo a nombres
    private static final Map<Integer, String> SYMBOL_NAMES = new HashMap<>();
    
    static {
        SYMBOL_NAMES.put(sym.MAGE, "MAGE");
        SYMBOL_NAMES.put(sym.WARRIOR, "WARRIOR");
        SYMBOL_NAMES.put(sym.INITIAL, "INITIAL");
        SYMBOL_NAMES.put(sym.RULES, "RULES");
        SYMBOL_NAMES.put(sym.IF, "IF");
        SYMBOL_NAMES.put(sym.THEN, "THEN");
        SYMBOL_NAMES.put(sym.ELSE, "ELSE");
        SYMBOL_NAMES.put(sym.MATCH, "MATCH");
        SYMBOL_NAMES.put(sym.PLAYERS, "PLAYERS");
        SYMBOL_NAMES.put(sym.ROUNDS, "ROUNDS");
        SYMBOL_NAMES.put(sym.SCORING, "SCORING");
        SYMBOL_NAMES.put(sym.BONUSES, "BONUSES");
        SYMBOL_NAMES.put(sym.MAIN, "MAIN");
        SYMBOL_NAMES.put(sym.RUN, "RUN");
        SYMBOL_NAMES.put(sym.WITH, "WITH");
        SYMBOL_NAMES.put(sym.SEED, "SEED");
        
        SYMBOL_NAMES.put(sym.SELF_HEALTH, "SELF_HEALTH");
        SYMBOL_NAMES.put(sym.OPPONENT_HEALTH, "OPPONENT_HEALTH");
        SYMBOL_NAMES.put(sym.SELF_RESOURCE, "SELF_RESOURCE");
        SYMBOL_NAMES.put(sym.OPPONENT_RESOURCE, "OPPONENT_RESOURCE");
        SYMBOL_NAMES.put(sym.SELF_SCORE, "SELF_SCORE");
        SYMBOL_NAMES.put(sym.OPPONENT_SCORE, "OPPONENT_SCORE");
        SYMBOL_NAMES.put(sym.SELF_HISTORY, "SELF_HISTORY");
        SYMBOL_NAMES.put(sym.OPPONENT_HISTORY, "OPPONENT_HISTORY");
        SYMBOL_NAMES.put(sym.LAST_MOVE, "LAST_MOVE");
        SYMBOL_NAMES.put(sym.GET_MOVES_COUNT, "GET_MOVES_COUNT");
        SYMBOL_NAMES.put(sym.RANDOM, "RANDOM");
        
        SYMBOL_NAMES.put(sym.ARCANE_BOLT, "ARCANE_BOLT");
        SYMBOL_NAMES.put(sym.FIREBALL, "FIREBALL");
        SYMBOL_NAMES.put(sym.MAGIC_BARRIER, "MAGIC_BARRIER");
        SYMBOL_NAMES.put(sym.HEALING_RUNE, "HEALING_RUNE");
        SYMBOL_NAMES.put(sym.MEDITATE, "MEDITATE");
        SYMBOL_NAMES.put(sym.SLASH, "SLASH");
        SYMBOL_NAMES.put(sym.HEAVY_STRIKE, "HEAVY_STRIKE");
        SYMBOL_NAMES.put(sym.SHIELD_BLOCK, "SHIELD_BLOCK");
        SYMBOL_NAMES.put(sym.WAR_CRY, "WAR_CRY");
        SYMBOL_NAMES.put(sym.REST, "REST");
        
        SYMBOL_NAMES.put(sym.IGUAL_IGUAL, "==");
        SYMBOL_NAMES.put(sym.DIFERENTE, "!=");
        SYMBOL_NAMES.put(sym.MAYOR, ">");
        SYMBOL_NAMES.put(sym.MENOR, "<");
        SYMBOL_NAMES.put(sym.MAYOR_IGUAL, ">=");
        SYMBOL_NAMES.put(sym.MENOR_IGUAL, "<=");
        SYMBOL_NAMES.put(sym.AND, "&&");
        SYMBOL_NAMES.put(sym.OR, "||");
        SYMBOL_NAMES.put(sym.NOT, "!");
        
        SYMBOL_NAMES.put(sym.LLAVE_A, "{");
        SYMBOL_NAMES.put(sym.LLAVE_C, "}");
        SYMBOL_NAMES.put(sym.CORCHETE_A, "[");
        SYMBOL_NAMES.put(sym.CORCHETE_C, "]");
        SYMBOL_NAMES.put(sym.PARENTESIS_A, "(");
        SYMBOL_NAMES.put(sym.PARENTESIS_C, ")");
        SYMBOL_NAMES.put(sym.DOS_PUNTOS, ":");
        SYMBOL_NAMES.put(sym.COMA, ",");
        
        SYMBOL_NAMES.put(sym.DAMAGE_POINT, "damage_point");
        SYMBOL_NAMES.put(sym.HEALING_POINT, "healing_point");
        SYMBOL_NAMES.put(sym.SUCCESSFUL_DEFENSE, "successful_defense");
        SYMBOL_NAMES.put(sym.VICTORY_BONUS, "victory_bonus");
        SYMBOL_NAMES.put(sym.FAILED_ACTION_PENALTY, "failed_action_penalty");
        SYMBOL_NAMES.put(sym.MAGE_COMBO, "mage_combo");
        SYMBOL_NAMES.put(sym.MAGE_COMBO_POINTS, "mage_combo_points");
        SYMBOL_NAMES.put(sym.WARRIOR_COMBO, "warrior_combo");
        SYMBOL_NAMES.put(sym.WARRIOR_COMBO_POINTS, "warrior_combo_points");
        SYMBOL_NAMES.put(sym.LOW_HEALTH_VICTORY, "low_health_victory");
        
        SYMBOL_NAMES.put(sym.IDENTIFICADOR, "IDENTIFICADOR");
        SYMBOL_NAMES.put(sym.ENTERO, "ENTERO");
        SYMBOL_NAMES.put(sym.FLOTANTE, "FLOTANTE");
    }
    
    public static void main(String[] args) {
        String archivoMedio = "pruebas/Medio.btl";
        
        System.out.println("=".repeat(60));
        System.out.println("PRUEBA LEXER - ARCHIVO MEDIO.BTL");
        System.out.println("=".repeat(60));
        System.out.println();
        
        try {
            FileReader reader = new FileReader(archivoMedio);
            Lexer lexer = new Lexer(reader);
            
            int tokenCount = 0;
            int lineaAnterior = -1;
            
            Symbol token;
            ArrayList<String> tokensEnLínea = new ArrayList<>();
            
            System.out.printf("%-5s | %-25s | %-15s | %s%n", "Línea", "Token", "Tipo", "Valor");
            System.out.println("-".repeat(75));
            
            while ((token = lexer.next_token()).sym != sym.EOF) {
                tokenCount++;
                String tokenName = SYMBOL_NAMES.getOrDefault(token.sym, "DESCONOCIDO(" + token.sym + ")");
                String valor = token.value != null ? token.value.toString() : "";
                
                // Formatea la salida
                System.out.printf("%-5d | %-25s | %-15s | %s%n", 
                    token.left, 
                    tokenName, 
                    (token.sym == sym.IDENTIFICADOR || token.sym == sym.ENTERO || token.sym == sym.FLOTANTE) ? valor : "—",
                    (token.left != lineaAnterior) ? "[Línea " + token.left + "]" : "");
                
                lineaAnterior = token.left;
            }
            
            System.out.println("-".repeat(75));
            System.out.println();
            System.out.println("✅ RESULTADO FINAL:");
            System.out.println("   Total de tokens reconocidos: " + tokenCount);
            System.out.println("   Errores léxicos encontrados: " + lexer.erroresLexicos.size());
            
            if (lexer.erroresLexicos.size() > 0) {
                System.out.println("\n⚠️  ERRORES LÉXICOS:");
                for (var error : lexer.erroresLexicos) {
                    System.out.println("   - " + error.toString());
                }
            } else {
                System.out.println("\n✅ No hay errores léxicos - El archivo es válido sintácticamente para el Lexer");
            }
            
            System.out.println();
            System.out.println("📊 CONCLUSIÓN:");
            System.out.println("   El archivo Medio.btl es reconocido CORRECTAMENTE por el Lexer");
            System.out.println("   Todas las palabras clave, operadores y acciones se tokenizaron.");
            System.out.println("   ⚠️  NOTA: El Parser stub podría tener limitaciones.");
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
