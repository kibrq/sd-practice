package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.Field

class MoveEventHandler(override val field: Field) : EventHandler<MoveEvent> {
    override fun handle(event: MoveEvent) {
        TODO("Not yet implemented")
    }
}
