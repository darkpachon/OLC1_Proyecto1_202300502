package motor;

/**
 * Clase que define las estadísticas base para cada clase de combatiente.
 * Mago y Guerrero tienen diferentes atributos de vida, recursos, ataque, defensa, etc.
 */
public class Estadisticas {
    
    // --- ESTADÍSTICAS DEL MAGO ---
    public static class Mago {
        public static final int VIDA_MAXIMA = 100;
        public static final int RECURSO_MAXIMO = 120;  // Maná
        public static final int ATAQUE_FISICO = 5;
        public static final int PODER_MAGICO = 25;
        public static final int ARMADURA = 8;
        public static final int RESISTENCIA_MAGICA = 18;
        public static final int VELOCIDAD = 14;
    }
    
    // --- ESTADÍSTICAS DEL GUERRERO ---
    public static class Guerrero {
        public static final int VIDA_MAXIMA = 140;
        public static final int RECURSO_MAXIMO = 100;  // Energía
        public static final int ATAQUE_FISICO = 22;
        public static final int PODER_MAGICO = 0;
        public static final int ARMADURA = 20;
        public static final int RESISTENCIA_MAGICA = 8;
        public static final int VELOCIDAD = 10;
    }
    
    /**
     * Obtiene la vida máxima según la clase.
     */
    public static int getVidaMaxima(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.VIDA_MAXIMA : Guerrero.VIDA_MAXIMA;
    }
    
    /**
     * Obtiene el recurso máximo según la clase.
     */
    public static int getRecursoMaximo(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.RECURSO_MAXIMO : Guerrero.RECURSO_MAXIMO;
    }
    
    /**
     * Obtiene el ataque físico según la clase.
     */
    public static int getAtaqueFisico(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.ATAQUE_FISICO : Guerrero.ATAQUE_FISICO;
    }
    
    /**
     * Obtiene el poder mágico según la clase.
     */
    public static int getPoderMagico(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.PODER_MAGICO : Guerrero.PODER_MAGICO;
    }
    
    /**
     * Obtiene la armadura según la clase.
     */
    public static int getArmadura(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.ARMADURA : Guerrero.ARMADURA;
    }
    
    /**
     * Obtiene la resistencia mágica según la clase.
     */
    public static int getResistenciaMagica(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.RESISTENCIA_MAGICA : Guerrero.RESISTENCIA_MAGICA;
    }
    
    /**
     * Obtiene la velocidad según la clase.
     */
    public static int getVelocidad(String clase) {
        return clase.equalsIgnoreCase("mage") ? Mago.VELOCIDAD : Guerrero.VELOCIDAD;
    }
}
