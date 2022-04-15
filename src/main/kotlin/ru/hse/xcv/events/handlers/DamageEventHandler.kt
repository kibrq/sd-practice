package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.world.World

class DamageEventHandler(override val world: World) : EventHandler<DamageEvent> {
    override fun handle(event: DamageEvent) {
        event.entity.damage(event.amount)
    }
}
