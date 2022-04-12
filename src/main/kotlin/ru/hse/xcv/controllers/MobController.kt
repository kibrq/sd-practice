package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.NoneEvent

import ru.hse.xcv.model.Mob
import ru.hse.xcv.world.World

interface MobStrategy {
    abstract val mob: Mob
    abstract val world: World

    fun takeAction(callback: ActionController): Event
}


class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World,
): MobStrategy {
    override fun takeAction(callback: ActionController): Event {
        return NoneEvent(callback)
    }
}


class MobController(
    private val strategy: MobStrategy,
    override val eventFactory: EventBus,
) : ActionController {
    override fun action() {
        eventFactory.fire(strategy.takeAction(this))
    }
}
