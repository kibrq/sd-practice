package ru.hse.xcv.events

import ru.hse.xcv.events.handlers.EventHandler

/*
 * Dispatches events of type T.
 */
class EventDispatcher<T : Event> {
    private var handlers = mutableListOf<EventHandler<T>>()

    /*
     * Register an event handler.
     */
    fun register(handler: EventHandler<T>) = handlers.add(handler)

    /*
     * Dispatches event by triggering each handler.
     */
    fun run(event: T) = handlers.forEach { it.handle(event) }
}
