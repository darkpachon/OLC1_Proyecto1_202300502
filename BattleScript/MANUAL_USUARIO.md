# Manual de Usuario - BattleScript

## Introducción

**BattleScript** es un lenguaje de programación y simulador de batallas donde puedes definir estrategias de combate para dos personajes (Mago o Guerrero) y enfrentarlos en un duelo.

## Componentes Principales

### 1. Definición de Estrategias

Una estrategia define cómo un combatiente actúa durante la batalla. Cada estrategia tiene:

- **Nombre**: Identificador único (ej. "Merlin", "Ragnar")
- **Clase**: `mage` (Mago) o `warrior` (Guerrero)
- **Acción Inicial**: La acción que realiza en la Ronda 0
- **Reglas**: Condiciones que determinan qué acción ejecutar
- **Acción por Defecto**: Acción cuando ninguna regla se cumple



### 2. Acciones Disponibles

#### Para Magos:
- `ARCANE_BOLT`: Ataque mágico básico (poder 20, costo 15)
- `FIREBALL`: Ataque mágico fuerte (poder 35, costo 25)
- `MAGIC_BARRIER`: Defensa mágica (costo 20)
- `HEALING_RUNE`: Curación (poder 25, costo 30)
- `MEDITATE`: Recupera recurso (poder 30, costo 10)

#### Para Guerreros:
- `SLASH`: Ataque físico básico (poder 20, costo 15)
- `HEAVY_STRIKE`: Ataque físico fuerte (poder 35, costo 25)
- `SHIELD_BLOCK`: Defensa física (costo 20)
- `WAR_CRY`: Mejora el próximo ataque +10 (costo 15)
- `REST`: Recupera vida (poder 25, costo 10)

### 3. Condiciones

Las condiciones usan operadores lógicos (`AND`, `OR`, `NOT`) y comparadores:

#### Variables de Estado:
- `SELF_HEALTH`: Tu vida actual
- `OPPONENT_HEALTH`: Vida del oponente
- `SELF_RESOURCE`: Tu recurso actual
- `OPPONENT_RESOURCE`: Recurso del oponente
- `SELF_SCORE`: Tu puntuación
- `OPPONENT_SCORE`: Puntuación del oponente
- `ROUND_NUMBER`: Número de ronda actual
- `TOTAL_ROUNDS`: Total de rondas de la partida
- `RANDOM`: Valor aleatorio entre 0.0 y 1.0

#### Operadores:
- `==` (IGUAL_IGUAL): Igualdad
- `!=` (DIFERENTE): Desigualdad
- `>` (MAYOR): Mayor que
- `<` (MENOR): Menor que
- `>=` (MAYOR_IGUAL): Mayor o igual
- `<=` (MENOR_IGUAL): Menor o igual


### 4. Ejecución

La sección ejecutar para las partidas:

## Sistema de Puntuación

La puntuación se calcula durante la batalla:

- **Por Daño**: `damage_point * daño_causado`
- **Por Curación**: `healing_point * vida_recuperada`
- **Por Defensa Exitosa**: `successful_defense` puntos
- **Por Victoria**: `victory_bonus` + bonos especiales
- **Por Acción Fallida**: `-failed_action_penalty`
- **Por Combo**: Puntos extra si se ejecuta la secuencia exacta

## Criterios de Victoria

1. **Derrota directa**: Primera vida ≤ 0 gana
2. **Mayor puntuación**: Si ambos sobreviven
3. **Mayor vida restante**: Si tienen igual puntuación
4. **Mayor recurso restante**: Desempate final

S

**¡Crea tus propias estrategias y domina el campo de batalla!**
