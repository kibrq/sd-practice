package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.model.Field

class DamageEventHandler(override val field: Field) : EventHandler<DamageEvent> {
    override fun handle(event: DamageEvent) {
        event.entity.receiveDamage(event.damage)
    }
}
