package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.CreateSpellEvent
import ru.hse.xcv.model.Field

class CreateSpellEventHandler(override val field: Field) : EventHandler<CreateSpellEvent> {
    override fun handle(event: CreateSpellEvent) {

    }
}
