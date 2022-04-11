package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.model.Field

interface EventHandler<T : Event> {
    val field: Field
    fun handle(event: T)
}
