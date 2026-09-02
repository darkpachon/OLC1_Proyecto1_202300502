package reportes;

import java.util.List;

public class GestorReportes {

    public static String generarTablaErrores(List<ErrorLexicoSintactico> errores) {
        if (errores == null || errores.isEmpty()) {
            return "=================================================\n"
                 + "¡Felicidades! No se encontraron errores.\n"
                 + "=================================================\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================================================\n");
        sb.append(String.format("%-12s | %-7s | %-9s | %-60s\n", "TIPO", "LÍNEA", "COLUMNA", "DESCRIPCIÓN"));
        sb.append("====================================================================================================\n");
        for (ErrorLexicoSintactico e : errores) {
            sb.append(String.format("%-12s | %-7d | %-9d | %-60s\n", e.getTipo(), e.getLinea(), e.getColumna(), e.getDescripcion()));
        }
        sb.append("====================================================================================================\n");
        sb.append("Total de errores: ").append(errores.size()).append("\n");
        return sb.toString();
    }

    public static String generarTablaTokens(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return "No se reconocieron tokens.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("LISTA DE TOKENS RECONOCIDOS\n");
        sb.append("=================================================\n");
        sb.append(String.format("%-6s | %s\n", "Nº", "TOKEN"));
        sb.append("-------------------------------------------------\n");
        int i = 1;
        for (String t : tokens) {
            sb.append(String.format("%-6d | %s\n", i++, t));
        }
        sb.append("-------------------------------------------------\n");
        sb.append("Total: ").append(tokens.size()).append("\n");
        return sb.toString();
    }

    // [FIX] Nuevo método sin parámetros para llamarlo desde el menú sin romper
    public static String generarTablaTokens() {
        return "No hay tokens disponibles. Realiza un análisis primero.\n"
             + "El reporte de tokens se generará al ejecutar el análisis.";
    }
}