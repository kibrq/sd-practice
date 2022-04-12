package ru.hse.xcv.controllers


import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.events.NoneEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

interface MobStrategy {
    val mob: Mob
    val world: World

    fun takeAction(callback: ActionController): Event
}


class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World,
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(callback: ActionController): Event {
        val (_, dyn) = world.readNeighbourhood(mob.position, Size.create(20, 20))
        return dyn.values.filterIsInstance<Hero>().firstOrNull()?.let {
            val dp = (it.position - mob.position).normalize()
            MoveEvent(mob, dp, false, callback)
        } ?: NoneEvent(callback)
    }
}


class MobController(
    private val strategy: MobStrategy,
    override val eventBus: EventBus,
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun action() {
        eventBus.fire(strategy.takeAction(this))
    }
}
