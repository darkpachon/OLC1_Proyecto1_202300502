package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Importamos las clases de nuestro analizador y reportes
import analizador.Lexer;
import analizador.Parser;
import reportes.GestorReportes;
import reportes.ErrorLexicoSintactico;

/**
 * Ventana principal del entorno de trabajo de BattleScript.
 * Contiene el editor de texto, consola de salida y visor de reportes.
 */
public class EditorFrame extends JFrame {

    // Áreas de texto principales
    private JTextArea areaEntrada;
    private JTextArea areaReporte;
    private JTextArea areaSalida;

    // Archivo actual abierto (para guardar rápidamente)
    private File archivoActual = null;

    public EditorFrame() {
        // Configuración básica de la ventana principal
        setTitle("BattleScript IDE - Compilador");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setLayout(new BorderLayout());

        // Inicializar los componentes
        inicializarMenu();
        inicializarPaneles();
    }

    /**
     * Construye la barra de menú superior con las opciones requeridas.
     */
    private void inicializarMenu() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menú Archivo ---
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        itemNuevo.addActionListener(e -> nuevoArchivo());
        
        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrir.addActionListener(e -> abrirArchivo());
        
        JMenuItem itemGuardar = new JMenuItem("Guardar Archivo");
        itemGuardar.addActionListener(e -> guardarArchivo());

        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemGuardar);

        // --- Menú Reportes ---
        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemReporteTokens = new JMenuItem("Reporte de Tokens");
        JMenuItem itemReporteErrores = new JMenuItem("Reporte de Errores");
        
        // CONEXIÓN CON EL GESTOR DE REPORTES
        itemReporteTokens.addActionListener(e -> areaReporte.setText(GestorReportes.generarTablaTokens()));
        
        itemReporteErrores.addActionListener(e -> {
            // En una implementación final, aquí obtendrías las listas reales de tu Lexer y Parser guardados en memoria.
            // Por ahora, mostraremos un ejemplo de cómo se verá la tabla si hay un error.
            List<ErrorLexicoSintactico> erroresEjemplo = new ArrayList<>();
            erroresEjemplo.add(new ErrorLexicoSintactico("Sintáctico", "Falta ';' al final de la instrucción", 10, 5));
            
            areaReporte.setText(GestorReportes.generarTablaErrores(erroresEjemplo));
        });

        menuReportes.add(itemReporteTokens);
        menuReportes.add(itemReporteErrores);

        // --- Menú Ejecutar ---
        JMenu menuEjecutar = new JMenu("Ejecutar");
        JMenuItem itemAnalizar = new JMenuItem("Analizar y Ejecutar");
        
        itemAnalizar.addActionListener(e -> ejecutarAnalisis());

        menuEjecutar.add(itemAnalizar);

        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        menuBar.add(menuEjecutar);

        setJMenuBar(menuBar);
    }

    /**
     * Construye y organiza las áreas de texto en la ventana.
     */
    private void inicializarPaneles() {
        // Áreas de texto
        areaEntrada = new JTextArea();
        areaReporte = new JTextArea();
        areaSalida = new JTextArea();

        // Cambiar colores para simular el diseño oscuro
        Color fondoOscuro = new Color(30, 30, 30);
        Color textoClaro = new Color(220, 220, 220);

        configurarEstiloArea(areaEntrada, fondoOscuro, textoClaro);
        configurarEstiloArea(areaReporte, fondoOscuro, textoClaro);
        configurarEstiloArea(areaSalida, new Color(20, 20, 20), Color.GREEN);

        areaReporte.setEditable(false);
        areaSalida.setEditable(false);

        // Paneles con Scroll
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Entrada", 0, 0, null, Color.WHITE));

        JScrollPane scrollReporte = new JScrollPane(areaReporte);
        scrollReporte.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Reporte", 0, 0, null, Color.WHITE));

        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Salida", 0, 0, null, Color.WHITE));

        // División Superior: Entrada (Izquierda) y Reporte (Derecha)
        JSplitPane splitSuperior = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollEntrada, scrollReporte);
        splitSuperior.setResizeWeight(0.6); // 60% para entrada, 40% para reporte
        splitSuperior.setBackground(Color.BLACK);

        // División Principal: Superior (Entrada/Reporte) e Inferior (Salida)
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitSuperior, scrollSalida);
        splitPrincipal.setResizeWeight(0.7); // 70% arriba, 30% abajo
        splitPrincipal.setBackground(Color.BLACK);

        // Fondo principal
        getContentPane().setBackground(Color.BLACK);
        add(splitPrincipal, BorderLayout.CENTER);
    }

    private void configurarEstiloArea(JTextArea area, Color fondo, Color texto) {
        area.setBackground(fondo);
        area.setForeground(texto);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }

    // --- ACCIONES DE LOS BOTONES ---

    private void nuevoArchivo() {
        areaEntrada.setText("");
        areaSalida.setText("");
        areaReporte.setText("");
        archivoActual = null;
        JOptionPane.showMessageDialog(this, "Nuevo archivo creado.");
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos BattleScript (*.btl)", "btl");
        fileChooser.setFileFilter(filtro);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoActual))) {
                areaEntrada.read(reader, null);
                areaSalida.setText("Archivo cargado con éxito: " + archivoActual.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos BattleScript (*.btl)", "btl");
            fileChooser.setFileFilter(filtro);

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoActual = fileChooser.getSelectedFile();
                // Asegurar extensión .btl
                if (!archivoActual.getName().toLowerCase().endsWith(".btl")) {
                    archivoActual = new File(archivoActual.getParentFile(), archivoActual.getName() + ".btl");
                }
            } else {
                return; // Usuario canceló
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoActual))) {
            areaEntrada.write(writer);
            areaSalida.setText("Archivo guardado con éxito: " + archivoActual.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Toma el código del editor y lo pasa por el analizador léxico y sintáctico.
     */
    private void ejecutarAnalisis() {
        String codigo = areaEntrada.getText();
        if (codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El área de entrada está vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaSalida.setText("Iniciando análisis léxico y sintáctico con JFlex y CUP...\n");
        
        try {
            // 1. Convertir el texto en un flujo de lectura
            StringReader lector = new StringReader(codigo);
            
            // 2. Pasar el texto al analizador léxico
            Lexer lexer = new Lexer(lector);
            
            // 3. Pasar el analizador léxico al analizador sintáctico
            Parser parser = new Parser(lexer);
            
            // 4. Ejecutar el análisis
            parser.parse();
            
            // 5. Evaluar los resultados
            if (lexer.erroresLexicos.isEmpty() && parser.erroresSintacticos.isEmpty()) {
                areaSalida.append("¡Análisis completado con éxito! No se encontraron errores.\n");
                areaSalida.append("El código es válido y está listo para la ejecución de las partidas.\n");
            } else {
                areaSalida.append("Se encontraron errores en el código.\n");
                areaSalida.append(" - Errores Léxicos: " + lexer.erroresLexicos.size() + "\n");
                areaSalida.append(" - Errores Sintácticos: " + parser.erroresSintacticos.size() + "\n");
                areaSalida.append("Revisa la sección de reportes para más detalles.\n");
            }
            
        } catch (Exception ex) {
            areaSalida.append("El análisis se detuvo debido a un error crítico irrecuperable.\n");
            ex.printStackTrace();
        }
    }
}