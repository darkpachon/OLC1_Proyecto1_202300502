package reportes;

import java.util.List;

public class GestorReportes {

    public static String generarTablaErrores(List<ErrorLexicoSintactico> errores) {
        if (errores == null || errores.isEmpty()) {
            return "=================================================\n"
                 + "¡Felicidades! No se encontraron errores.\n"
                 + "El código está completamente limpio.\n"
                 + "=================================================\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================================================\n");
        sb.append(String.format("%-12s | %-7s | %-9s | %-60s\n", "TIPO", "LÍNEA", "COLUMNA", "DESCRIPCIÓN"));
        sb.append("====================================================================================================\n");

        for (ErrorLexicoSintactico error : errores) {
            sb.append(String.format("%-12s | %-7d | %-9d | %-60s\n", 
                    error.getTipo(), 
                    error.getLinea(), 
                    error.getColumna(), 
                    error.getDescripcion()));
        }

        sb.append("====================================================================================================\n");
        sb.append("Total de errores detectados: ").append(errores.size()).append("\n");
        return sb.toString();
    }

    /**
     * Genera una tabla con la lista de tokens.
     */
    public static String generarTablaTokens(List<TokenInfo> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return "No hay tokens para mostrar. Ejecuta 'Analizar y Ejecutar' primero.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════╤═══════════════════╤════════════════════════════════╤════════╤══════════╗\n");
        sb.append("║  #   │ Tipo              │ Lexema                         │ Línea  │ Columna  ║\n");
        sb.append("╠══════╪═══════════════════╪════════════════════════════════╪════════╪══════════╣\n");

        for (TokenInfo t : tokens) {
            sb.append(String.format("║ %4d │ %-17s │ %-30s │ %6d │ %8d ║\n",
                    t.getNumero(),
                    t.getTipo().length() > 17 ? t.getTipo().substring(0, 17) : t.getTipo(),
                    t.getLexema().length() > 30 ? t.getLexema().substring(0, 30) : t.getLexema(),
                    t.getLinea(),
                    t.getColumna()));
        }

        sb.append("╚══════╧═══════════════════╧════════════════════════════════╧════════╧══════════╝\n");
        sb.append("Total de tokens: ").append(tokens.size());
        return sb.toString();
    }
}