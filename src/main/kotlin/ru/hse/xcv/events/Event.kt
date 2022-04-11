package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.controllers.ActionController
import ru.hse.xcv.model.DynamicObject

sealed interface Event {
    val callback: ActionController?
}

data class NoneEvent(
    override val callback: ActionController?
) : Event

data class MoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    override val callback: ActionController?,
) : Event

data class BuffEvent(
    val obj: DynamicObject,
    override val callback: ActionController?,
) : Event

data class CreateSpellEvent(
    val obj: DynamicObject,
    override val callback: ActionController?,
) : Event

data class DamageEvent(
    val obj: DynamicObject,
    override val callback: ActionController?,
) : Event

data class LetterPressed(
    val letter: Char,
    override val callback: ActionController?,
) : Event
