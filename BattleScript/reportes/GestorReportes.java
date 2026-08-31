package reportes;

import java.util.List;

/**
 * Clase encargada de dar formato a los datos del compilador
 * para mostrarlos visualmente en la interfaz gráfica.
 */
public class GestorReportes {

    /**
     * Genera una tabla de texto con todos los errores encontrados.
     * 
     * @param errores Lista de errores léxicos y sintácticos combinados.
     * @return Cadena de texto formateada como tabla.
     */
    public static String generarTablaErrores(List<ErrorLexicoSintactico> errores) {
        if (errores == null || errores.isEmpty()) {
            return "=================================================\n"
                 + "¡Felicidades! No se encontraron errores.\n"
                 + "El código está completamente limpio.\n"
                 + "=================================================\n";
        }

        StringBuilder sb = new StringBuilder();
        
        // Encabezado de la tabla
        sb.append("====================================================================================================\n");
        sb.append(String.format("%-12s | %-7s | %-9s | %-60s\n", "TIPO", "LÍNEA", "COLUMNA", "DESCRIPCIÓN"));
        sb.append("====================================================================================================\n");

        // Filas de la tabla
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
     * Espacio preparado para el reporte de tokens (símbolos válidos).
     * 
     * @return Cadena de texto con el formato de la tabla.
     */
    public static String generarTablaTokens() {
        return "El reporte de tokens se implementará próximamente.\n"
             + "(Requiere almacenar los tokens reconocidos desde el archivo Lexer.jflex)";
    }
}