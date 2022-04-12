package ru.hse.xcv.controllers


import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Size

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.NoneEvent
import ru.hse.xcv.events.MoveEvent

import ru.hse.xcv.model.entities.*
import ru.hse.xcv.world.World

import ru.hse.xcv.util.normalize

interface MobStrategy {
    val mob: Mob
    val world: World

    fun takeAction(callback: ActionController): Event
}


class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World,
): MobStrategy {
    
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(callback: ActionController): Event {
        val (_, dyn) = world.readNeighbourhood(mob.position, Size.create(20, 20))
        val hero = dyn.values.filterIsInstance<Hero>().firstOrNull()
        if (hero != null) {
            val dp = (hero.position - mob.position).normalize()
            return MoveEvent(mob, dp, false, callback)
        }
        return NoneEvent(callback)
    }
}


class MobController(
    private val strategy: MobStrategy,
    override val eventFactory: EventBus,
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun action() {
        eventFactory.fire(strategy.takeAction(this))
    }
}
