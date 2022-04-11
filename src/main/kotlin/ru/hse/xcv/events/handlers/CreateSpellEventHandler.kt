package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.BuffEvent
import ru.hse.xcv.events.Event
import ru.hse.xcv.model.Field

class CreateSpellEventHandler(override val field: Field) : EventHandler<BuffEvent> {
    override fun handle(event: BuffEvent) {
        TODO("Not yet implemented")
    }
}
