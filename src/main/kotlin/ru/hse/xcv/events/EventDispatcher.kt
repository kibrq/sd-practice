package ru.hse.xcv.events

import ru.hse.xcv.events.handlers.EventHandler

class EventDispatcher<T : Event> {
    private var handlers = mutableListOf<EventHandler<T>>()

    fun register(handler: EventHandler<T>) = handlers.add(handler)

    fun run(event: T) = handlers.forEach { it.handle(event) }
}
