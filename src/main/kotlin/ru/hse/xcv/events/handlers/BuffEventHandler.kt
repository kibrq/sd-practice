package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.BuffEvent
import ru.hse.xcv.world.World

/*
 * Handles BuffEvent.
 */
class BuffEventHandler(override val world: World) : GameEventHandler<BuffEvent> {
    /*
     * Buffs `event.entity` by `event.buff`.
     */
    override fun handle(event: BuffEvent) {
        event.entity.stats += event.buff
    }
}
