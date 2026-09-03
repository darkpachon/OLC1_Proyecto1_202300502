# BattleScript - Simulador de Duelos Estratégicos

**BattleScript** es un lenguaje de programación especializado para definir estrategias de combate y simular duelos entre personajes mágicos y guerreros.

## 📋 Descripción del Proyecto

Este proyecto implementa un **analizador léxico-sintáctico completo** (`Lexer` + `Parser`) y un **motor de simulación** que evalúa estrategias de combate definidas en un lenguaje de dominio específico.

### Características Principales

✅ **Análisis Léxico**: Tokenización completa con JFlex  
✅ **Análisis Sintáctico**: Parsing con CUP (Java CUP)  
✅ **AST (Árbol de Sintaxis Abstracta)**: Representación de estrategias  
✅ **Motor de Simulación**: Ejecución de batallas ronda por ronda  
✅ **Evaluador de Condiciones**: Evaluación recursiva de reglas  
✅ **Sistema de Puntuación**: Cálculo dinámico de puntos  
✅ **Determinismo**: Reproducibilidad con seeds  

## 📦 Estructura de Carpetas

```
BattleScript/
├── bin/                              # Archivos compilados
├── lib/                              # Librerías externas
│   ├── java-cup-11b-runtime.jar     # Runtime de CUP
│   ├── java-cup-11b.jar             # Herramienta CUP
│   └── jflex-full-1.9.1.jar        # Herramienta JFlex
├── pruebas/                          # Archivos de prueba
│   └── prueba1.btl                  # Ejemplo: Merlin vs Ragnar
├── src/
│   ├── Main.java                    # Punto de entrada
│   ├── ast/                         # AST Classes
│   │   ├── Estrategia.java
│   │   ├── Partida.java
│   │   ├── Regla.java
│   │   ├── NodoLogico.java
│   │   └── NodoRelacional.java
│   ├── motor/                       # Motor de simulación
│   │   ├── Simulador.java
│   │   ├── EvaluadorCondiciones.java
│   │   ├── EstadoCombatiente.java
│   │   ├── MotorCombate.java
│   │   ├── GestorPuntuacion.java
│   │   ├── Accion.java
│   │   ├── Estadisticas.java
│   │   └── ResultadoRonda.java
│   ├── analizador/                  # Lexer & Parser
│   │   ├── Lexer.jflex
│   │   ├── Lexer.java (generado)
│   │   ├── Parser.cup
│   │   ├── Parser.java (generado)
│   │   └── sym.java (generado)
│   ├── ui/                          # Interfaz gráfica
│   │   └── EditorFrame.java
│   └── reportes/
│       └── ErrorLexicoSintactico.java
├── build.xml                         # Configuración de compilación
├── MANUAL_USUARIO.md                 # Manual de usuario
├── MANUAL_TECNICO.md                 # Manual técnico
└── README.md                         # Este archivo
```

## 🚀 Inicio Rápido

### 1. Compilar el Proyecto

```bash
cd BattleScript
javac -d bin -cp "lib/java-cup-11b-runtime.jar" $(find src -name "*.java")
```

### 2. Ejecutar la Simulación

```bash
java -cp "bin;lib/java-cup-11b-runtime.jar" Main
```

### 3. Crear tu Propio Archivo de Prueba

Crea un archivo `.btl` (ej. `mi_duelo.btl`):

```battlescript
mage Hechicero {
    initial: ARCANE_BOLT
    rules: [
        if (SELF_HEALTH < 40) then HEALING_RUNE,
        if (OPPONENT_HEALTH > 70) then FIREBALL,
        else: ARCANE_BOLT
    ]
}

warrior Espadachin {
    initial: SLASH
    rules: [
        if (SELF_HEALTH < 50) then REST,
        if (OPPONENT_HEALTH > 80) then HEAVY_STRIKE,
        else: SLASH
    ]
}

match MiPrueeba {
    players: [Hechicero, Espadachin]
    rounds: 15
    scoring: {
        damage_point: 2,
        healing_point: 3,
        successful_defense: 5,
        victory_bonus: 20,
        failed_action_penalty: 3
    }
    bonuses: {
        mage_combo: [FIREBALL, ARCANE_BOLT, FIREBALL],
        mage_combo_points: 15,
        warrior_combo: [SLASH, HEAVY_STRIKE, SLASH],
        warrior_combo_points: 15,
        low_health_victory: 10
    }
}

main {
    run [MiPrueba] with {
        seed: 123
    }
}
```

## 📚 Documentación

- **[MANUAL_USUARIO.md](MANUAL_USUARIO.md)**: Guía completa para usar BattleScript
- **[MANUAL_TECNICO.md](MANUAL_TECNICO.md)**: Documentación técnica y arquitectura

## 🎮 Ejemplo de Salida

```
=================================================
=== INICIANDO PARTIDA: DueloPrueba ===
Semilla (Seed): 42
Jugador 1: Merlin (mage)
Jugador 2: Ragnar (warrior)
Rondas totales: 10
=================================================

--- RONDA 0 (Acción Inicial) ---
Merlin: ARCANE_BOLT
Ragnar: SLASH
  ⚔️ Merlin usa ARCANE_BOLT (daño: 29).
  ⚔️ Ragnar usa SLASH (daño: 26).
  [ESTADO] Merlin: 74 HP / 110 Recurso  |  Ragnar: 111 HP / 90 Recurso

--- RONDA 1 ---
Merlin: FIREBALL
Ragnar: HEAVY_STRIKE
  ⚔️ Merlin usa FIREBALL (daño: 42).
  ⚔️ Ragnar usa HEAVY_STRIKE (daño: 39).
  [ESTADO] Merlin: 35 HP / 80 Recurso | Puntos: 284  |  Ragnar: 69 HP / 65 Recurso | Puntos: 130

...

=================================================
=== FIN DE LA PARTIDA ===
🏆 ¡GANADOR: Merlin!
   Razón: Mayor puntuación
=================================================
```

## 🔧 Herramientas Utilizadas

| Herramienta | Versión | Propósito |
|------------|---------|----------|
| JFlex | 1.9.1 | Análisis léxico (Lexer) |
| Java CUP | 11b | Análisis sintáctico (Parser) |
| Java | 11+ | Lenguaje de programación |

## 🎯 Características del Lenguaje

### Clases de Personajes

- **Mago (mage)**: 103 HP, 110 Recurso
  - Especializado en magia (+15% daño)
  - 5 acciones mágicas

- **Guerrero (warrior)**: 137 HP, 90 Recurso
  - Especializado en física (+15% defensa)
  - 5 acciones físicas

### Tipos de Acciones

| Tipo | Descripción | Efecto |
|------|-------------|--------|
| Ataque | Causa daño al oponente | -Vida enemigo |
| Curación | Recupera tu vida | +Tu vida |
| Defensa | Reduce daño recibido | -40% daño próxima ronda |
| Mejora | Aumenta daño del próximo ataque | +Daño próxima acción |
| Recuperación | Restaura recurso | +Tu recurso |

### Operadores Lógicos y Relacionales

- **Lógicos**: `AND`, `OR`, `NOT`
- **Relacionales**: `==`, `!=`, `>`, `<`, `>=`, `<=`
- **Variables de Estado**: `SELF_HEALTH`, `OPPONENT_HEALTH`, `ROUND_NUMBER`, etc.

## 📊 Sistema de Puntuación

La puntuación se calcula dinámicamente:

- **Daño Causado**: `damagePoint × daño`
- **Vida Curada**: `healingPoint × vida`
- **Defensa Exitosa**: `successfulDefense` puntos
- **Combo Ejecutado**: `comboPoints` puntos
- **Acción Fallida**: `-failedActionPenalty` puntos
- **Victoria**: `victoryBonus` + bonos especiales

## 🏆 Criterios de Victoria

1. **Primer K.O.**: Una vida ≤ 0
2. **Mayor Puntuación**: Si ambos sobreviven
3. **Mayor Vida Restante**: Desempate
4. **Mayor Recurso Restante**: Desempate final

## 📝 Requisitos Previos

- Java JDK 11 o superior
- Compilador javac disponible en el PATH

## 🛠️ Regenerar Lexer y Parser

Si modificas los archivos `.jflex` o `.cup`:

```bash
# Generar Lexer
java -jar lib/jflex-full-1.9.1.jar -d src/analizador src/analizador/Lexer.jflex

# Generar Parser
java -cp lib/java-cup-11b.jar java_cup.Main -destdir src/analizador src/analizador/Parser.cup

# Recompilar todo
javac -d bin -cp "lib/java-cup-11b-runtime.jar" $(find src -name "*.java")
```

## 📄 Licencia

Proyecto académico - Curso: Organización de Lenguajes y Compiladores 1 (2026)

## 👨‍💻 Autor

**Pablo** - Implementación completa del lenguaje y simulador

---

**¿Listo para crear tu estrategia ganadora? ¡Comienza ahora!** 🎮
