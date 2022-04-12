package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.CreateSpellEvent
import ru.hse.xcv.world.World

class CreateSpellEventHandler(override val world: World) : EventHandler<CreateSpellEvent> {
    override fun handle(event: CreateSpellEvent) {

    }
}
