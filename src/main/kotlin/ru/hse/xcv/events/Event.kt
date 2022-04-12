package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.controllers.ActionController
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.spells.Spell
import ru.hse.xcv.model.stats.Stats

sealed interface Event {
    val callback: ActionController?
}

data class NoneEvent(
    override val callback: ActionController?
) : Event

data class MoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    val moveWorld: Boolean = false,
    override val callback: ActionController?
) : Event

data class BuffEvent(
    val entity: Entity,
    val buff: Stats,
    override val callback: ActionController?
) : Event

data class CreateSpellEvent(
    val spell: Spell,
    val position: Position,
    override val callback: ActionController?
) : Event

data class DamageEvent(
    val entity: Entity,
    val damage: Int,
    override val callback: ActionController?
) : Event

data class LetterPressedEvent(
    val letter: Char,
    override val callback: ActionController?
) : Event
