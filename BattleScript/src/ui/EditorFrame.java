package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import analizador.Lexer;
import analizador.Parser;
import analizador.sym;
import reportes.ErrorLexicoSintactico;
import reportes.GestorReportes;
import reportes.TokenInfo;
import java_cup.runtime.Symbol;

import ast.*;
import motor.*;

public class EditorFrame extends JFrame {

    private JTextArea areaEntrada;
    private JTextArea areaReporte;
    private JTextArea areaSalida;

    private File archivoActual = null;

    private List<TokenInfo> ultimosTokens = new ArrayList<>();
    private List<ErrorLexicoSintactico> ultimosErrores = new ArrayList<>();

    public EditorFrame() {
        setTitle("BattleScript IDE - Compilador");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarMenu();
        inicializarPaneles();
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

        itemReporteTokens.addActionListener(e -> mostrarReporteTokens());
        itemReporteErrores.addActionListener(e -> mostrarReporteErrores());

        menuReportes.add(itemReporteTokens);
        menuReportes.add(itemReporteErrores);

        JMenu menuEjecutar = new JMenu("Ejecutar");
        JMenuItem itemAnalizar = new JMenuItem("Analizar y Ejecutar");
        itemAnalizar.addActionListener(e -> ejecutarAnalisisYSimulacion());

        menuEjecutar.add(itemAnalizar);

        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        menuBar.add(menuEjecutar);

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

    private void nuevoArchivo() {
        areaEntrada.setText("");
        areaSalida.setText("");
        areaReporte.setText("");
        archivoActual = null;
        limpiarDatosAnalisis();
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
                limpiarDatosAnalisis();
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
                if (!archivoActual.getName().toLowerCase().endsWith(".btl")) {
                    archivoActual = new File(archivoActual.getParentFile(), archivoActual.getName() + ".btl");
                }
            } else {
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoActual))) {
            areaEntrada.write(writer);
            areaSalida.setText("Archivo guardado con éxito: " + archivoActual.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarDatosAnalisis() {
        ultimosTokens.clear();
        ultimosErrores.clear();
    }

    private void ejecutarAnalisisYSimulacion() {
        String codigo = areaEntrada.getText();
        if (codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El área de entrada está vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaSalida.setText("");
        limpiarDatosAnalisis();

        // Mostrar primeras líneas del código para depuración
        areaSalida.append("=== INICIO ANÁLISIS ===\n");
        String[] lineas = codigo.split("\n");
        int maxLineas = Math.min(5, lineas.length);
        for (int i = 0; i < maxLineas; i++) {
            areaSalida.append("L" + (i+1) + ": " + lineas[i] + "\n");
        }
        areaSalida.append("... (total " + lineas.length + " líneas)\n\n");

        // --- 1. Análisis léxico ---
        try {
            areaSalida.append("Creando lexer...\n");
            StringReader lector = new StringReader(codigo);
            Lexer lexer = new Lexer(lector);
            areaSalida.append("Lexer creado. Iniciando lectura de tokens...\n");

            int contador = 0;
            Symbol s = null;
            boolean eofAlcanzado = false;
            int maxTokens = 10000;

            while (!eofAlcanzado && contador < maxTokens) {
                try {
                    s = lexer.next_token();
                    contador++;
                    if (s.sym == sym.EOF) {
                        eofAlcanzado = true;
                        areaSalida.append("EOF alcanzado.\n");
                        break;
                    }
                    if (s.sym != sym.error) {
                        String nombreToken = obtenerNombreToken(s.sym);
                        String lexema = (s.value != null) ? s.value.toString() : "";
                        int linea = s.left;
                        int columna = s.right;
                        ultimosTokens.add(new TokenInfo(contador, nombreToken, lexema, linea, columna));
                    }
                } catch (Exception ex) {
                    areaSalida.append("❌ Excepción al leer token " + (contador+1) + ": " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                    // Mostrar la traza en el área de salida (primeras líneas)
                    StackTraceElement[] stack = ex.getStackTrace();
                    for (int i = 0; i < Math.min(5, stack.length); i++) {
                        areaSalida.append("   " + stack[i].toString() + "\n");
                    }
                    throw ex; // relanzamos para salir del bucle y reportar error
                }
            }

            if (contador >= maxTokens) {
                areaSalida.append("⚠️ Límite de tokens alcanzado (" + maxTokens + ").\n");
            }

            // Capturar errores léxicos del lexer
            ultimosErrores.addAll(lexer.erroresLexicos);

            areaSalida.append("Análisis léxico completado.\n");
            areaSalida.append("  Tokens: " + ultimosTokens.size() + "\n");
            areaSalida.append("  Errores: " + ultimosErrores.size() + "\n");

            if (!ultimosErrores.isEmpty()) {
                areaSalida.append("⚠️ Hay errores léxicos. No se ejecutará la simulación.\n");
                areaReporte.setText(GestorReportes.generarTablaErrores(ultimosErrores));
                return;
            }

            areaSalida.append("✅ Sin errores léxicos.\n\n");

            // Mostrar reporte de tokens
            areaReporte.setText(GestorReportes.generarTablaTokens(ultimosTokens));

        } catch (Exception ex) {
            areaSalida.append("❌ ERROR CRÍTICO en el análisis léxico:\n");
            areaSalida.append("   " + ex.getMessage() + "\n");
            ex.printStackTrace();
            return;
        }

        // --- 2. Simulación ---
        areaSalida.append("=== EJECUTANDO SIMULACIÓN ===\n");
        try {
            String resultado = ejecutarSimulacionPrueba();
            areaSalida.append(resultado);
        } catch (Exception ex) {
            areaSalida.append("❌ Error en la simulación: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    // ==================== MÉTODOS DE SIMULACIÓN ====================

    private Estrategia crearEstrategiaMago() {
        Estrategia mago = new Estrategia("Merlin", "mage");
        mago.setAccionInicial("ARCANE_BOLT");

        List<Regla> reglas = new ArrayList<>();
        reglas.add(new Regla(new NodoRelacional("SELF_HEALTH", "MENOR", 30), "HEALING_RUNE"));
        reglas.add(new Regla(new NodoRelacional("OPPONENT_HEALTH", "MAYOR", 50), "FIREBALL"));
        reglas.add(new Regla(new NodoRelacional("ROUND_NUMBER", "MAYOR", 5), "MEDITATE"));

        mago.setReglas(reglas);
        mago.setAccionPorDefecto("ARCANE_BOLT");
        return mago;
    }

    private Estrategia crearEstrategiaGuerrero() {
        Estrategia guerrero = new Estrategia("Ragnar", "warrior");
        guerrero.setAccionInicial("SLASH");

        List<Regla> reglas = new ArrayList<>();
        reglas.add(new Regla(new NodoRelacional("SELF_HEALTH", "MENOR", 25), "REST"));
        reglas.add(new Regla(new NodoRelacional("OPPONENT_HEALTH", "MAYOR", 60), "HEAVY_STRIKE"));
        reglas.add(new Regla(new NodoRelacional("ROUND_NUMBER", "MAYOR", 3), "WAR_CRY"));

        guerrero.setReglas(reglas);
        guerrero.setAccionPorDefecto("SLASH");
        return guerrero;
    }

    private Partida crearPartidaPrueba() {
        Partida partida = new Partida("DueloPrueba");
        partida.setRondas(10);
        partida.setDamagePoint(2);
        partida.setHealingPoint(3);
        partida.setSuccessfulDefense(5);
        partida.setVictoryBonus(20);
        partida.setFailedActionPenalty(3);
        return partida;
    }

    private String ejecutarSimulacionPrueba() {
        Estrategia merlin = crearEstrategiaMago();
        Estrategia ragnar = crearEstrategiaGuerrero();
        Partida partida = crearPartidaPrueba();
        Simulador sim = new Simulador(partida, merlin, ragnar, 42);
        return sim.ejecutarPartida();
    }

    // ==================== MÉTODOS PARA REPORTES ====================

    private String obtenerNombreToken(int symCode) {
        try {
            return sym.terminalNames[symCode];
        } catch (Exception e) {
            return "TOKEN_" + symCode;
        }
    }

    private void mostrarReporteTokens() {
        if (ultimosTokens.isEmpty()) {
            areaReporte.setText("⚠️ No hay tokens. Ejecuta 'Analizar y Ejecutar' primero.");
            return;
        }
        areaReporte.setText(GestorReportes.generarTablaTokens(ultimosTokens));
    }

    private void mostrarReporteErrores() {
        if (ultimosErrores.isEmpty()) {
            areaReporte.setText("✅ No se encontraron errores.");
            return;
        }
        areaReporte.setText(GestorReportes.generarTablaErrores(ultimosErrores));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}