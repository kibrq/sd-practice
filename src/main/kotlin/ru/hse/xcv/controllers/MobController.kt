package ru.hse.xcv.controllers


import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

interface MobStrategy {
    val mob: Mob
    val world: World

    fun takeAction(): Event?
}


class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World,
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(): Event? {
        val rect = Size.create(20, 20)
        return world.nearestObjectInNeighbourhood(mob.position, rect, Hero::class)?.let {
            val dp = (it.position - mob.position).normalize()
            MoveEvent(mob, dp, moveWorld = false)
        }
    }
}


class MobController(
    private val strategy: MobStrategy,
    override val eventBus: EventBus,
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun action(): Boolean {
        if (strategy.mob.isDead()) return false
        val event = strategy.takeAction() ?: return true
        eventBus.fire(event)
        return true
    }
}
