import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.EditorFrame;

/**
 * Clase principal que inicia la aplicación BattleScript.
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Intentar usar el estilo visual del sistema operativo (opcional, pero se ve mejor)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema del sistema. Se usará el tema por defecto.");
        }

        // Iniciar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            EditorFrame ventanaPrincipal = new EditorFrame();
            ventanaPrincipal.setVisible(true);
        });
    }
}