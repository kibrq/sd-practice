package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.Field

class MoveEventHandler(override val field: Field) : EventHandler<MoveEvent> {
    override fun handle(event: MoveEvent) {
        repeat(event.obj.moveSpeed) {
            event.obj.position += event.offset
        }
    }
}
