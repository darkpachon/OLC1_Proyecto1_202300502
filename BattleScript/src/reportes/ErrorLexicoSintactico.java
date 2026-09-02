package reportes;

/**
 * Clase para representar errores léxicos y sintácticos encontrados durante el análisis.
 */
public class ErrorLexicoSintactico {
    
    private String tipo;          // "Léxico" o "Sintáctico"
    private String descripcion;   // Descripción del error
    private int linea;            // Línea donde ocurrió el error
    private int columna;          // Columna donde ocurrió el error
    
    public ErrorLexicoSintactico(String tipo, String descripcion, int linea, int columna) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }
    
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
    
    @Override
    public String toString() {
        return "[" + tipo + "] Línea " + linea + ", Columna " + columna + ": " + descripcion;
    }
}
