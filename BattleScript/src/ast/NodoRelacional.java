package ast;

/**
 * Clase que representa un nodo relacional en el AST.
 * Se utiliza para comparaciones (==, !=, >, <, >=, <=) entre expresiones.
 */
public class NodoRelacional {
    
    private Object izquierda;   // Expresión izquierda (puede ser String, Integer, o variable)
    private String operador;    // Operador de comparación: "==", "!=", ">", "<", ">=", "<="
    private Object derecha;     // Expresión derecha (puede ser String, Integer, o variable)
    
    /**
     * Constructor de NodoRelacional.
     * 
     * @param izquierda La expresión izquierda de la comparación
     * @param operador El operador de comparación
     * @param derecha La expresión derecha de la comparación
     */
    public NodoRelacional(Object izquierda, String operador, Object derecha) {
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }
    
    // --- GETTERS Y SETTERS ---
    
    public Object getIzquierda() {
        return izquierda;
    }
    
    public void setIzquierda(Object izquierda) {
        this.izquierda = izquierda;
    }
    
    public String getOperador() {
        return operador;
    }
    
    public void setOperador(String operador) {
        this.operador = operador;
    }
    
    public Object getDerecha() {
        return derecha;
    }
    
    public void setDerecha(Object derecha) {
        this.derecha = derecha;
    }
    
    /**
     * Método toString para depuración.
     */
    @Override
    public String toString() {
        return izquierda + " " + operador + " " + derecha;
    }
}
