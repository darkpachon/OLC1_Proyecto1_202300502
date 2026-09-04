# Manual Técnico - BattleScript

## Arquitectura del Proyecto

```
BattleScript/
├── src/
│   ├── Main.java                    # Punto de entrada principal
│   ├── ast/                         # Árbol de Sintaxis Abstracta
│   │   ├── Estrategia.java         # Definición de estrategia
│   │   ├── Partida.java            # Configuración de match
│   │   ├── Regla.java              # Regla if-then-else
│   │   ├── NodoLogico.java         # Nodos AND/OR/NOT
│   │   └── NodoRelacional.java     # Nodos de comparación
│   ├── motor/                       # Motor de simulación
│   │   ├── Simulador.java          # Orquestador principal
│   │   ├── EvaluadorCondiciones.java # Evaluador de condiciones
│   │   ├── EstadoCombatiente.java  # Estado durante batalla
│   │   ├── GestorPuntuacion.java   # Cálculo de puntos
│   │   ├── MotorCombate.java       # Lógica de combate
│   │   ├── Accion.java             # Enum de acciones
│   │   ├── Estadisticas.java       # Stats por clase
│   │   └── ResultadoRonda.java     # Resultado de cada ronda
│   ├── analizador/                  # Análisis léxico-sintáctico
│   │   ├── Lexer.jflex             # Definición de tokens
│   │   ├── Lexer.java              # Generado por JFlex
│   │   ├── Parser.cup              # Definición de gramática
│   │   ├── Parser.java             # Generado por CUP
│   │   └── sym.java                # Símbolos de tokens
│   ├── ui/                          # Interfaz gráfica
│   │   └── EditorFrame.java        # Editor visual
│   └── reportes/                    # Manejo de errores
│       └── ErrorLexicoSintactico.java
├── bin/                             # Compilados
├── lib/                             # Librerías externas
│   ├── java-cup-11b.jar
│   ├── java-cup-11b-runtime.jar
│   └── jflex-full-1.9.1.jar
├── pruebas/                         # Archivos de prueba
│   └── prueba1.btl
├── MANUAL_USUARIO.md
└── MANUAL_TECNICO.md (este archivo)
```

## Componentes Principales

### 1. AST (Árbol de Sintaxis Abstracta)

**Estrategia** (`ast/Estrategia.java`)
- Almacena nombre, clase, acciones iniciales, reglas
- Estructura jerárquica: nombre → clase → reglas

**Regla** (`ast/Regla.java`)
- Representa `if condición then acción`
- La condición es un Object (puede ser NodoLogico o NodoRelacional)
- La acción es un String (nombre de la acción)

**NodoLogico** (`ast/NodoLogico.java`)
- Representa operaciones lógicas: AND, OR, NOT
- Árbol binario (izquierda y derecha)

**NodoRelacional** (`ast/NodoRelacional.java`)
- Representa comparaciones: ==, !=, >, <, >=, <=
- Compara dos expresiones

### 2. Motor de Simulación

**Simulador** (`motor/Simulador.java`)
- Orquestador principal de la batalla
- Ejecuta rondas secuencialmente
- Ronda 0: Acciones iniciales (sin evaluación de reglas)
- Rondas 1-N: Evaluación de reglas + resolución de acciones

**EvaluadorCondiciones** (`motor/EvaluadorCondiciones.java`)
- Evalúa recursivamente el árbol de condiciones
- Resuelve variables de estado
- Maneja operadores lógicos y relacionales

**EstadoCombatiente** (`motor/EstadoCombatiente.java`)
- Almacena estado actual: vida, recurso, puntos, historial
- Mantiene bonificaciones temporales (defensa, WAR_CRY)
- Limita vida y recurso a máximos

**MotorCombate** (`motor/MotorCombate.java`)
- Calcula daño (físico/mágico)
- Aplica reducciones por defensa
- Determina prioridad de acciones
- Aplica curaciones y recuperaciones

**GestorPuntuacion** (`motor/GestorPuntuacion.java`)
- Calcula puntos por cada tipo de acción
- Detecta combos
- Aplica bonos de victoria
- Implementa penalizaciones

**Accion** (`motor/Accion.java`)
- Enum con 10 acciones
- Propiedades: nombre, poder, costo, tipo, prioridad

### 3. Análisis Léxico-Sintáctico

**Lexer.jflex** (`analizador/Lexer.jflex`)
- Define tokens: palabras clave, operadores, literales
- Procesado por JFlex → `Lexer.java`

**Parser.cup** (`analizador/Parser.cup`)
- Define gramática BNF
- Especifica producciones de AST
- Procesado por CUP → `Parser.java` + `sym.java`

## Flujo de Ejecución

```
Main.java
  ↓
Crea Estrategias (mago/guerrero)
Crea Partida (configuración)
  ↓
Simulador.ejecutarPartida()
  ├─ Ronda 0 (Acción Inicial)
  │  ├─ Ejecuta acción inicial de ambos
  │  └─ Calcula efectos
  ├─ Rondas 1-N (Con reglas)
  │  ├─ EvaluadorCondiciones evalúa reglas
  │  ├─ Determina acciones
  │  ├─ Resuelve acciones en orden de prioridad
  │  ├─ GestorPuntuacion actualiza puntos
  │  └─ Verifica si alguien murió
  └─ determinarGanador()
     ├─ Derrota directa
     ├─ Mayor puntuación
     ├─ Mayor vida
     └─ Mayor recurso
  ↓
Retorna log formateado con resultado
```

## Clases de Combatientes

### Mago (mage)
- **Vida Máxima**: 103 HP
- **Recurso Máximo**: 110
- **Clase**: Especializado en magia
- **Bonus de Daño Mágico**: +15%

### Guerrero (warrior)
- **Vida Máxima**: 137 HP
- **Recurso Máximo**: 90
- **Clase**: Especializado en física
- **Bonus de Defensa**: -15% daño recibido

## Cálculo de Daño

### Daño Físico
```
danoBase = poder + (poder * 0.1) * claseAtacante
danoConBonus = danoBase + bonificacionWARCRY
danoFinal = danoConBonus - (danoConBonus * 0.15 * claseDefensor)

Si defendiendo:
  danoFinal *= 0.6  (reducción 40%)
```

### Daño Mágico
```
danoBase = poder + (poder * 0.15) * claseAtacante
danoFinal = danoBase

Si defendiendo:
  danoFinal *= 0.5  (reducción 50%)
```

## Sistema de Puntuación

Cada acción genera puntos:

| Acción | Fórmula | Puntos |
|--------|---------|--------|
| Daño | `damagePoint * dañoCausado` | Variable |
| Curación | `healingPoint * vidaRecuperada` | Variable |
| Defensa | `successfulDefense` | Fijo |
| Combo | `comboPoints` | Fijo |
| Acción Fallida | `-failedActionPenalty` | Fijo |
| Victoria | `victoryBonus + bonusVidaBaja` | Variable |

## Generación de Código

### JFlex (Lexer)
```bash
java -jar lib/jflex-full-1.9.1.jar -d src/analizador src/analizador/Lexer.jflex
```

### CUP (Parser)
```bash
java -cp lib/java-cup-11b.jar java_cup.Main -destdir src/analizador src/analizador/Parser.cup
```

## Compilación

```bash
javac -d bin -cp "lib/java-cup-11b-runtime.jar" $(find src -name "*.java")
```

## Ejecución

```bash
java -cp "bin;lib/java-cup-11b-runtime.jar" Main
```

## Estructura de Archivos de Prueba

```battlescript
mage NOMBRE {
    initial: ACCIÓN
    rules: [
        if (CONDICIÓN) then ACCIÓN,
        else: ACCIÓN
    ]
}

warrior NOMBRE {
    initial: ACCIÓN
    rules: [
        if (CONDICIÓN) then ACCIÓN,
        else: ACCIÓN
    ]
}

match NOMBRE {
    players: [ESTRATEGIA1, ESTRATEGIA2]
    rounds: NUM
    scoring: { ... }
    bonuses: { ... }
}

main {
    run [PARTIDA] with {
        seed: NÚMERO
    }
}
```

## Notas Importantes

1. **Determinismo**: Con la misma seed, el resultado es idéntico
2. **Generadores Independientes**: Cada estrategia tiene su propio Random
3. **Historial**: Se registran acciones ejecutadas exitosamente
4. **Penalizaciones**: Acciones fallidas restan puntos pero no se registran en historial
5. **Ronda 0 Especial**: Las acciones iniciales se ejecutan sin evaluar reglas

---

**Desarrollado para: Organización de Lenguajes y Compiladores 1**
**Año: 2026**
