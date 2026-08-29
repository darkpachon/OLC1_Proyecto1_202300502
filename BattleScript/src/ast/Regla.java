package ast;

/**
 * Clase que representa una regla condicional (if-then) dentro de una Estrategia.
 * Se encarga de guardar la expresión lógica y la acción que se ejecutará si se cumple.
 */
public class Regla {
    
    // Representa la condición a evaluar (el bloque después del 'if').
    // Por ahora es Object para evitar errores de compilación, luego será un Nodo de Expresión.
    private Object condicion; 
    
    // Representa la acción que se realizará si la condición es verdadera (el bloque después del 'then').
    private String accion;

    /**
     * Constructor principal de la Regla.
     * 
     * @param condicion La expresión booleana que se debe cumplir.
     * @param accion La habilidad o movimiento que se ejecutará.
     */
    public Regla(Object condicion, String accion) {
        this.condicion = condicion;
        this.accion = accion;
    }

    // --- GETTERS Y SETTERS ---

    public Object getCondicion() {
        return condicion;
    }

    public void setCondicion(Object condicion) {
        this.condicion = condicion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Método auxiliar para imprimir la regla en consola de forma legible.
     * Útil para verificar que el AST se está armando correctamente.
     */
    @Override
    public String toString() {
        String textoCondicion = (condicion != null) ? condicion.toString() : "null";
        return "if (" + textoCondicion + ") then " + accion;
    }
}