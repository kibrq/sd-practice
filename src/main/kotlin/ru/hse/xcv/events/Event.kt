package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.spells.Spell
import ru.hse.xcv.model.stats.Stats

sealed interface Event

object NoneEvent : Event // do we really need it? (consider using `Event?`)

data class MoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    val moveWorld: Boolean = false
) : Event

data class BuffEvent(
    val entity: Entity,
    val buff: Stats
) : Event

data class CreateSpellEvent(
    val spell: Spell,
    val position: Position,
    val direction: Position,
    val level: Int
) : Event

data class DamageEvent(
    val entity: Entity,
    val amount: Int
) : Event

data class LetterPressedEvent(
    val letter: Char
) : Event
