package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.world.World

interface EventHandler<T : Event> {
    val world: World
    fun handle(event: T)
}
