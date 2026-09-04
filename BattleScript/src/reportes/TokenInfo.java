package reportes;

public class TokenInfo {
    private int numero;
    private String tipo;
    private String lexema;
    private int linea;
    private int columna;

    public TokenInfo(int numero, String tipo, String lexema, int linea, int columna) {
        this.numero = numero;
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public String getLexema() { return lexema; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
}