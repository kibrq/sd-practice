package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.spells.Spell
import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.view.State

sealed interface Event

data class MoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    val moveWorld: Boolean = false,
    val crazyMovements: Boolean = false
) : Event

data class BuffEvent(
    val entity: Entity,
    val buff: Stats
) : Event

data class CastSpellEvent(
    val spell: Spell,
    val position: Position,
    val direction: Position,
    val power: Int
) : Event

class HPChangeEvent private constructor(
    val entity: Entity,
    val amount: Int
) : Event {
    companion object {
        fun createDamageEvent(entity: Entity, damage: Int) = HPChangeEvent(entity, -damage)
        fun createHealEvent(entity: Entity, heal: Int) = HPChangeEvent(entity, heal)
    }
}

data class LetterPressedEvent(
    val letter: Char
) : Event

data class SwitchScreenEvent(
    val newState: State.Type
) : Event

data class ScrollInventoryEvent(
    val dPos: Int
): Event
