package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.BuffEvent
import ru.hse.xcv.world.World

class BuffEventHandler(override val world: World) : GameEventHandler<BuffEvent> {
    override fun handle(event: BuffEvent) {
        event.entity.stats += event.buff
    }
}
