package analizador;

/**
 * Símbolos (tokens) para CUP generados por java_cup.
 * Este es un stub que permite la compilación del Lexer.java generado por JFlex.
 */
public class sym {
    
    // Estructura y Control
    public static final int MAGE = 1;
    public static final int WARRIOR = 2;
    public static final int INITIAL = 3;
    public static final int RULES = 4;
    public static final int IF = 5;
    public static final int THEN = 6;
    public static final int ELSE = 7;
    public static final int MATCH = 8;
    public static final int PLAYERS = 9;
    public static final int ROUNDS = 10;
    public static final int SCORING = 11;
    public static final int BONUSES = 12;
    public static final int MAIN = 13;
    public static final int RUN = 14;
    public static final int WITH = 15;
    public static final int SEED = 16;
    
    // Tipos de Datos
    public static final int ENTERO = 17;
    public static final int FLOTANTE = 18;
    public static final int IDENTIFICADOR = 19;
    public static final int TRUE = 20;
    public static final int FALSE = 21;
    
    // Puntuación y Configuración
    public static final int DAMAGE_POINT = 22;
    public static final int HEALING_POINT = 23;
    public static final int SUCCESSFUL_DEFENSE = 24;
    public static final int VICTORY_BONUS = 25;
    public static final int FAILED_ACTION_PENALTY = 26;
    public static final int MAGE_COMBO = 27;
    public static final int MAGE_COMBO_POINTS = 28;
    public static final int WARRIOR_COMBO = 29;
    public static final int WARRIOR_COMBO_POINTS = 30;
    public static final int LOW_HEALTH_VICTORY = 31;
    
    // Acciones - Mago
    public static final int ARCANE_BOLT = 32;
    public static final int FIREBALL = 33;
    public static final int MAGIC_BARRIER = 34;
    public static final int HEALING_RUNE = 35;
    public static final int MEDITATE = 36;
    
    // Acciones - Guerrero
    public static final int SLASH = 37;
    public static final int HEAVY_STRIKE = 38;
    public static final int SHIELD_BLOCK = 39;
    public static final int WAR_CRY = 40;
    public static final int REST = 41;
    
    // Estados
    public static final int ROUND_NUMBER = 42;
    public static final int TOTAL_ROUNDS = 43;
    public static final int SELF_HEALTH = 44;
    public static final int OPPONENT_HEALTH = 45;
    public static final int SELF_RESOURCE = 46;
    public static final int OPPONENT_RESOURCE = 47;
    public static final int SELF_SCORE = 48;
    public static final int OPPONENT_SCORE = 49;
    public static final int RANDOM = 50;
    
    // Historia
    public static final int SELF_HISTORY = 51;
    public static final int OPPONENT_HISTORY = 52;
    public static final int GET_MOVE = 53;
    public static final int LAST_MOVE = 54;
    public static final int GET_MOVES_COUNT = 55;
    public static final int GET_LAST_N_MOVES = 56;
    
    // Operadores
    public static final int IGUAL_IGUAL = 57;
    public static final int DIFERENTE = 58;
    public static final int MAYOR = 59;
    public static final int MENOR = 60;
    public static final int MAYOR_IGUAL = 61;
    public static final int MENOR_IGUAL = 62;
    public static final int AND = 63;
    public static final int OR = 64;
    public static final int NOT = 65;
    
    // Delimitadores
    public static final int LLAVE_A = 66;
    public static final int LLAVE_C = 67;
    public static final int CORCHETE_A = 68;
    public static final int CORCHETE_C = 69;
    public static final int PARENTESIS_A = 70;
    public static final int PARENTESIS_C = 71;
    public static final int DOS_PUNTOS = 72;
    public static final int COMA = 73;
    
    // Especiales
    public static final int EOF = 74;
    public static final int error = 75;

    // Nombres de los tokens (debe coincidir con los números anteriores)
    public static final String[] terminalNames = {
        "EOF", "error",
        "MAGE", "WARRIOR", "INITIAL", "RULES", "IF", "THEN", "ELSE", "MATCH",
        "PLAYERS", "ROUNDS", "SCORING", "BONUSES", "MAIN", "RUN", "WITH", "SEED",
        "ENTERO", "FLOTANTE", "IDENTIFICADOR", "TRUE", "FALSE",
        "DAMAGE_POINT", "HEALING_POINT", "SUCCESSFUL_DEFENSE", "VICTORY_BONUS", "FAILED_ACTION_PENALTY",
        "MAGE_COMBO", "MAGE_COMBO_POINTS", "WARRIOR_COMBO", "WARRIOR_COMBO_POINTS", "LOW_HEALTH_VICTORY",
        "ARCANE_BOLT", "FIREBALL", "MAGIC_BARRIER", "HEALING_RUNE", "MEDITATE",
        "SLASH", "HEAVY_STRIKE", "SHIELD_BLOCK", "WAR_CRY", "REST",
        "ROUND_NUMBER", "TOTAL_ROUNDS", "SELF_HEALTH", "OPPONENT_HEALTH",
        "SELF_RESOURCE", "OPPONENT_RESOURCE", "SELF_SCORE", "OPPONENT_SCORE", "RANDOM",
        "SELF_HISTORY", "OPPONENT_HISTORY", "GET_MOVE", "LAST_MOVE", "GET_MOVES_COUNT", "GET_LAST_N_MOVES",
        "IGUAL_IGUAL", "DIFERENTE", "MAYOR", "MENOR", "MAYOR_IGUAL", "MENOR_IGUAL", "AND", "OR", "NOT",
        "LLAVE_A", "LLAVE_C", "CORCHETE_A", "CORCHETE_C", "PARENTESIS_A", "PARENTESIS_C",
        "DOS_PUNTOS", "COMA"
    };
}