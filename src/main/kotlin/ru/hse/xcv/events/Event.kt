package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.controllers.ActionController
import ru.hse.xcv.model.DynamicObject

interface Event {
    val callback: ActionController?
}

interface GameEvent : Event

data class NoneEvent(
    override val callback: ActionController?,
) : Event

data class GameMoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    override val callback: ActionController?,
) : GameEvent

data class GameLetterPronounced(
    val letter: Char,
    override val callback: ActionController?,
) : Event
