package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World
import kotlin.math.abs

class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeSingleAction(): Event? {
        val hero = mob.findHero(world) ?: return null
        val offset = hero.position - mob.position
        val (dx, dy) = offset
        return if (abs(dx) <= 1 && abs(dy) <= 1) {
            null
        } else {
            MoveEvent(mob, offset.normalize(), moveWorld = false)
        }
    }
}

class AggressiveMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = AggressiveMobStrategy(mob, world)
}
