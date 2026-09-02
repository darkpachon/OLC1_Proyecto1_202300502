package ui;

import analizador.Lexer;
import analizador.Parser;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import reportes.ErrorLexicoSintactico;
import reportes.GestorReportes;

/**
 * Ventana principal del entorno de trabajo de BattleScript.
 * Contiene el editor de texto, consola de salida y visor de reportes.
 */
public class EditorFrame extends JFrame {

    private JTextArea areaEntrada;
    private JTextArea areaReporte;
    private JTextArea areaSalida;
    private File archivoActual = null;

    public EditorFrame() {
        setTitle("BattleScript IDE - Compilador");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarMenu();
        inicializarPaneles();

        // Redirigir la salida estándar y de error al área de texto
        redirigirSalida();
    }

    /**
     * Redirige System.out y System.err hacia el área de salida de la interfaz.
     */
    private void redirigirSalida() {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    areaSalida.append(String.valueOf((char) b));
                    areaSalida.setCaretPosition(areaSalida.getDocument().getLength());
                });
            }
        });
        System.setOut(ps);
        System.setErr(ps);
    }

    private void inicializarMenu() {
        JMenuBar menuBar = new JMenuBar();

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

        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemReporteTokens = new JMenuItem("Reporte de Tokens");
        JMenuItem itemReporteErrores = new JMenuItem("Reporte de Errores");
        itemReporteTokens.addActionListener(e -> areaReporte.setText(GestorReportes.generarTablaTokens()));
        itemReporteErrores.addActionListener(e -> {
            // Ejemplo: mostrar errores sintácticos del parser si existen
            // Por ahora usamos lista de ejemplo
            List<ErrorLexicoSintactico> erroresEjemplo = new ArrayList<>();
            erroresEjemplo.add(new ErrorLexicoSintactico("Sintáctico", "Falta ';' al final de la instrucción", 10, 5));
            areaReporte.setText(GestorReportes.generarTablaErrores(erroresEjemplo));
        });
        menuReportes.add(itemReporteTokens);
        menuReportes.add(itemReporteErrores);

        JMenu menuEjecutar = new JMenu("Ejecutar");
        JMenuItem itemAnalizar = new JMenuItem("Analizar y Ejecutar");
        itemAnalizar.addActionListener(e -> ejecutarAnalisisYSimulacion());
        menuEjecutar.add(itemAnalizar);

        // Menú Herramientas
        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem itemLimpiarSalida = new JMenuItem("Limpiar Salida");
        itemLimpiarSalida.addActionListener(e -> areaSalida.setText(""));
        menuHerramientas.add(itemLimpiarSalida);

        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        menuBar.add(menuEjecutar);
        menuBar.add(menuHerramientas);
        setJMenuBar(menuBar);
    }

    private void inicializarPaneles() {
        areaEntrada = new JTextArea();
        areaReporte = new JTextArea();
        areaSalida = new JTextArea();

        Color fondoOscuro = new Color(30, 30, 30);
        Color textoClaro = new Color(220, 220, 220);

        configurarEstiloArea(areaEntrada, fondoOscuro, textoClaro);
        configurarEstiloArea(areaReporte, fondoOscuro, textoClaro);
        configurarEstiloArea(areaSalida, new Color(20, 20, 20), Color.GREEN);

        areaReporte.setEditable(false);
        areaSalida.setEditable(false);

        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Entrada", 0, 0, null, Color.WHITE));

        JScrollPane scrollReporte = new JScrollPane(areaReporte);
        scrollReporte.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Reporte", 0, 0, null, Color.WHITE));

        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Salida", 0, 0, null, Color.WHITE));

        JSplitPane splitSuperior = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollEntrada, scrollReporte);
        splitSuperior.setResizeWeight(0.6);
        splitSuperior.setBackground(Color.BLACK);

        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitSuperior, scrollSalida);
        splitPrincipal.setResizeWeight(0.7);
        splitPrincipal.setBackground(Color.BLACK);

        getContentPane().setBackground(Color.BLACK);
        add(splitPrincipal, BorderLayout.CENTER);
    }

    private void configurarEstiloArea(JTextArea area, Color fondo, Color texto) {
        area.setBackground(fondo);
        area.setForeground(texto);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }

    // --- ACCIONES DE MENÚ ---

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
            try {
                // Leer todo el contenido del archivo como String
                String contenido = new String(Files.readAllBytes(archivoActual.toPath()), "UTF-8");
                areaEntrada.setText(contenido);
                areaSalida.setText("Archivo cargado con éxito: " + archivoActual.getName());
                areaReporte.setText(""); // Limpiar reporte al cargar nuevo archivo
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                areaSalida.setText("Error al cargar el archivo.");
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
                if (!archivoActual.getName().toLowerCase().endsWith(".btl")) {
                    archivoActual = new File(archivoActual.getParentFile(), archivoActual.getName() + ".btl");
                }
            } else {
                return;
            }
        }
        try {
            Files.write(archivoActual.toPath(), areaEntrada.getText().getBytes("UTF-8"));
            areaSalida.setText("Archivo guardado con éxito: " + archivoActual.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ejecuta el análisis léxico-sintáctico y, si no hay errores, lanza la simulación.
     */
    private void ejecutarAnalisisYSimulacion() {
        String codigo = areaEntrada.getText();
        if (codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El área de entrada está vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaSalida.setText("");
        areaSalida.append("Iniciando análisis léxico y sintáctico...\n");

        try {
            StringReader lector = new StringReader(codigo);
            Lexer lexer = new Lexer(lector);
            Parser parser = new Parser(lexer);
            // Intentar parsear
            Object ast = parser.parse();

            // Verificar errores
            if (lexer.erroresLexicos.isEmpty() && parser.erroresSintacticos.isEmpty()) {
                areaSalida.append("✅ Análisis exitoso. No se encontraron errores.\n");
                areaSalida.append("🚀 Iniciando simulación...\n\n");

                // Cuando el parser esté completo, descomenta la línea:
                // ejecutarSimulacionReal(parser);

                // Por ahora, usamos el ejemplo
                ejecutarSimulacionEjemplo();

            } else {
                areaSalida.append("❌ Se encontraron errores en el código.\n");
                areaSalida.append("   Léxicos: " + lexer.erroresLexicos.size() + "\n");
                areaSalida.append("   Sintácticos: " + parser.erroresSintacticos.size() + "\n");
                areaSalida.append("Revisa la pestaña de Reportes para más detalles.\n");
                // Mostrar errores en el reporte si se desea
                if (!lexer.erroresLexicos.isEmpty()) {
                    areaReporte.setText(GestorReportes.generarTablaErrores(lexer.erroresLexicos));
                } else if (!parser.erroresSintacticos.isEmpty()) {
                    areaReporte.setText(GestorReportes.generarTablaErrores(parser.erroresSintacticos));
                }
            }

        } catch (Exception ex) {
            areaSalida.append("💥 Error crítico durante el análisis.\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            areaSalida.append(sw.toString());
        }
    }

    /**
     * Simulación real usando el parser (cuando esté implementado).
     * Por ahora no se usa porque el parser es un stub.
     */
    private void ejecutarSimulacionReal(Parser parser) {
        // Obtener las estrategias y partidas del parser
        // var estrategias = parser.getEstrategias();
        // var partidas = parser.getPartidas();
        // var main = parser.getMainEjecucion();
        // ... ejecutar simulador con esos datos
        // Ejemplo:
        // if (!partidas.isEmpty()) {
        //     Partida partida = partidas.get(0);
        //     Estrategia jugador1 = estrategias.get(0);
        //     Estrategia jugador2 = estrategias.get(1);
        //     int seed = 123; // obtener del main
        //     Simulador simulador = new Simulador(partida, jugador1, jugador2, seed);
        //     String resultado = simulador.ejecutarPartida();
        //     System.out.println(resultado);
        // }
        System.out.println("⚠️ El parser aún no está implementado. No se puede ejecutar la simulación real.");
    }

    /**
     * EJEMPLO de simulación que usa System.out (redirigido a la interfaz).
     * Reemplázalo con tu lógica real cuando el parser funcione.
     */
    private void ejecutarSimulacionEjemplo() {
        System.out.println("=================================================");
        System.out.println("=== SIMULACIÓN DE DUELO (ejemplo) ===");
        System.out.println("Jugador 1: Merlin (mage)");
        System.out.println("Jugador 2: Ragnar (warrior)");
        System.out.println("Rondas totales: 10");
        System.out.println("=================================================\n");

        for (int r = 0; r < 10; r++) {
            System.out.println("--- Ronda " + (r + 1) + " ---");
            System.out.println("Merlin: ARCANE_BOLT (daño: 29)");
            System.out.println("Ragnar: SLASH (daño: 26)");
            System.out.println("  [ESTADO] Merlin: 74 HP / 110 Recurso  |  Ragnar: 111 HP / 90 Recurso");
        }

        System.out.println("\n=================================================");
        System.out.println("🏆 ¡GANADOR: Merlin!");
        System.out.println("=================================================");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}