package ru.hse.xcv.handlers

import ru.hse.xcv.events.GameEvent

import ru.hse.xcv.model.Field
import ru.hse.xcv.view.GameWorld

abstract class GameEventHandler<E : GameEvent> {
    protected abstract val field: Field
    protected abstract val world: GameWorld

    fun process(event: E) {
        handle(event)
        event.callback?.action()
    }

    abstract fun handle(event: E)
}
