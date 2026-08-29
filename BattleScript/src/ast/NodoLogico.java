package ast;

/**
 * Nodo del AST que representa operaciones lógicas (AND, OR, NOT).
 */
public class NodoLogico {
    
    private String operador;
    private Object izquierda; 
    private Object derecha;

    /**
     * Constructor para operadores binarios (AND, OR).
     */
    public NodoLogico(Object izquierda, String operador, Object derecha) {
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    /**
     * Constructor para operadores unarios (NOT).
     */
    public NodoLogico(String operador, Object unico) {
        this.operador = operador;
        this.izquierda = unico; 
        this.derecha = null;
    }

    public String getOperador() {
        return operador;
    }

    public Object getIzquierda() {
        return izquierda;
    }

    public Object getDerecha() {
        return derecha;
    }

    @Override
    public String toString() {
        if (derecha == null) {
            return operador + "(" + izquierda.toString() + ")";
        }
        return "(" + izquierda.toString() + " " + operador + " " + derecha.toString() + ")";
    }
}