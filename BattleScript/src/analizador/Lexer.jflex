package analizador;

import java_cup.runtime.*;
import reportes.ErrorLexicoSintactico;
import java.util.ArrayList;

%%

%public
%class Lexer
%cup
%line
%column
%unicode

%{
    // Lista para almacenar los errores léxicos y mostrarlos en el reporte
    public ArrayList<ErrorLexicoSintactico> erroresLexicos = new ArrayList<>();

    // Método auxiliar para generar tokens con su valor (lexema)
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    // Método auxiliar para generar tokens sin valor específico (como llaves o comas)
    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }
%}

// --- EXPRESIONES REGULARES ---
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Entero         = [0-9]+
Flotante       = [0-9]+\.[0-9]+
Identificador  = [a-zA-Z_][a-zA-Z0-9_]*

// --- ESTADOS DE JFLEX ---
// Estado para controlar que los comentarios multilínea se cierren correctamente
%state COMENTARIO_MULTILINEA

%%

// --- REGLAS LÉXICAS EN ESTADO NORMAL ---
<YYINITIAL> {
    
    /* Palabras Reservadas - Estructura */
    "mage"                  { return symbol(sym.MAGE, yytext()); }
    "warrior"               { return symbol(sym.WARRIOR, yytext()); }
    "initial"               { return symbol(sym.INITIAL, yytext()); }
    "rules"                 { return symbol(sym.RULES, yytext()); }
    "if"                    { return symbol(sym.IF, yytext()); }
    "then"                  { return symbol(sym.THEN, yytext()); }
    "else"                  { return symbol(sym.ELSE, yytext()); }
    "match"                 { return symbol(sym.MATCH, yytext()); }
    "players"               { return symbol(sym.PLAYERS, yytext()); }
    "rounds"                { return symbol(sym.ROUNDS, yytext()); }
    "scoring"               { return symbol(sym.SCORING, yytext()); }
    "bonuses"               { return symbol(sym.BONUSES, yytext()); }
    "main"                  { return symbol(sym.MAIN, yytext()); }
    "run"                   { return symbol(sym.RUN, yytext()); }
    "with"                  { return symbol(sym.WITH, yytext()); }
    "seed"                  { return symbol(sym.SEED, yytext()); }

    /* Palabras Reservadas - Funciones */
    "get_move"              { return symbol(sym.GET_MOVE, yytext()); }
    "last_move"             { return symbol(sym.LAST_MOVE, yytext()); }
    "get_moves_count"       { return symbol(sym.GET_MOVES_COUNT, yytext()); }
    "get_last_n_moves"      { return symbol(sym.GET_LAST_N_MOVES, yytext()); }

    /* Palabras Reservadas - Estados del sistema */
    "round_number"          { return symbol(sym.ROUND_NUMBER, yytext()); }
    "total_rounds"          { return symbol(sym.TOTAL_ROUNDS, yytext()); }
    "self_health"           { return symbol(sym.SELF_HEALTH, yytext()); }
    "opponent_health"       { return symbol(sym.OPPONENT_HEALTH, yytext()); }
    "self_resource"         { return symbol(sym.SELF_RESOURCE, yytext()); }
    "opponent_resource"     { return symbol(sym.OPPONENT_RESOURCE, yytext()); }
    "self_score"            { return symbol(sym.SELF_SCORE, yytext()); }
    "opponent_score"        { return symbol(sym.OPPONENT_SCORE, yytext()); }
    "self_history"          { return symbol(sym.SELF_HISTORY, yytext()); }
    "opponent_history"      { return symbol(sym.OPPONENT_HISTORY, yytext()); }
    "random"                { return symbol(sym.RANDOM, yytext()); }

    /* Palabras Reservadas - Configuración de Puntuación */
    "damage_point"          { return symbol(sym.DAMAGE_POINT, yytext()); }
    "healing_point"         { return symbol(sym.HEALING_POINT, yytext()); }
    "successful_defense"    { return symbol(sym.SUCCESSFUL_DEFENSE, yytext()); }
    "victory_bonus"         { return symbol(sym.VICTORY_BONUS, yytext()); }
    "failed_action_penalty" { return symbol(sym.FAILED_ACTION_PENALTY, yytext()); }
    "mage_combo"            { return symbol(sym.MAGE_COMBO, yytext()); }
    "mage_combo_points"     { return symbol(sym.MAGE_COMBO_POINTS, yytext()); }
    "warrior_combo"         { return symbol(sym.WARRIOR_COMBO, yytext()); }
    "warrior_combo_points"  { return symbol(sym.WARRIOR_COMBO_POINTS, yytext()); }
    "low_health_victory"    { return symbol(sym.LOW_HEALTH_VICTORY, yytext()); }

    /* Acciones - Mago */
    "ARCANE_BOLT"           { return symbol(sym.ARCANE_BOLT, yytext()); }
    "FIREBALL"              { return symbol(sym.FIREBALL, yytext()); }
    "MAGIC_BARRIER"         { return symbol(sym.MAGIC_BARRIER, yytext()); }
    "HEALING_RUNE"          { return symbol(sym.HEALING_RUNE, yytext()); }
    "MEDITATE"              { return symbol(sym.MEDITATE, yytext()); }

    /* Acciones - Guerrero */
    "SLASH"                 { return symbol(sym.SLASH, yytext()); }
    "HEAVY_STRIKE"          { return symbol(sym.HEAVY_STRIKE, yytext()); }
    "SHIELD_BLOCK"          { return symbol(sym.SHIELD_BLOCK, yytext()); }
    "WAR_CRY"               { return symbol(sym.WAR_CRY, yytext()); }
    "REST"                  { return symbol(sym.REST, yytext()); }

    /* Tipos de Datos / Literales */
    "true"                  { return symbol(sym.TRUE, yytext()); }
    "false"                 { return symbol(sym.FALSE, yytext()); }

    /* Operadores de Comparación y Lógicos */
    "=="                    { return symbol(sym.IGUAL_IGUAL, yytext()); }
    "!="                    { return symbol(sym.DIFERENTE, yytext()); }
    ">="                    { return symbol(sym.MAYOR_IGUAL, yytext()); }
    "<="                    { return symbol(sym.MENOR_IGUAL, yytext()); }
    ">"                     { return symbol(sym.MAYOR, yytext()); }
    "<"                     { return symbol(sym.MENOR, yytext()); }
    "&&"                    { return symbol(sym.AND, yytext()); }
    "||"                    { return symbol(sym.OR, yytext()); }
    "!"                     { return symbol(sym.NOT, yytext()); }

    /* Signos de Agrupación y Puntuación */
    "{"                     { return symbol(sym.LLAVE_A, yytext()); }
    "}"                     { return symbol(sym.LLAVE_C, yytext()); }
    "["                     { return symbol(sym.CORCHETE_A, yytext()); }
    "]"                     { return symbol(sym.CORCHETE_C, yytext()); }
    "("                     { return symbol(sym.PARENTESIS_A, yytext()); }
    ")"                     { return symbol(sym.PARENTESIS_C, yytext()); }
    ":"                     { return symbol(sym.DOS_PUNTOS, yytext()); }
    ","                     { return symbol(sym.COMA, yytext()); }

    /* Literales e Identificadores (Reglas generales al final para no ocultar palabras reservadas) */
    {Flotante}              { return symbol(sym.FLOTANTE, yytext()); }
    {Entero}                { return symbol(sym.ENTERO, yytext()); }
    {Identificador}         { return symbol(sym.IDENTIFICADOR, yytext()); }

    /* Comentario de una línea (Termina al encontrar un salto de línea) */
    "//".*                  { /* Ignorar, no se hace return */ }

    /* Comienzo de un comentario multilínea (Cambia de estado) */
    "/*"                    { yybegin(COMENTARIO_MULTILINEA); }

    /* Espacios en blanco */
    {WhiteSpace}            { /* Ignorar */ }

    /* Captura de Errores Léxicos (Cualquier símbolo que no coincida con lo anterior) */
    [^]                     { 
                              erroresLexicos.add(new ErrorLexicoSintactico("Léxico", "El carácter '" + yytext() + "' no pertenece al lenguaje", yyline + 1, yycolumn + 1)); 
                            }
}

// --- REGLAS LÉXICAS EN ESTADO DE COMENTARIO MULTILÍNEA ---
<COMENTARIO_MULTILINEA> {
    
    /* Si encuentra el cierre, regresa al estado normal */
    "*/"                    { yybegin(YYINITIAL); }
    
    /* Ignora todo el contenido mientras esté adentro */
    [^]                     { /* Ignorar */ }
    
    /* Si llega al fin del archivo sin cerrar el comentario, marca error y detiene */
    <<EOF>>                 { 
                              erroresLexicos.add(new ErrorLexicoSintactico("Léxico", "Comentario multilínea '/*' no fue cerrado", yyline + 1, yycolumn + 1));
                              return symbol(sym.EOF);
                            }
}