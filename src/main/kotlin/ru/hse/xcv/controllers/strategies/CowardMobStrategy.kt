package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

/*
 * CowardMobStrategy is a strategy of a coward mob.
 */
class CowardMobStrategy(
    override val mob: Mob,
    override val world: World
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Runs away from the hero if it is in mob's field of view.
     */
    override fun takeSingleAction(): Event? {
        val hero = mob.findHero(world) ?: return null
        val offset = (mob.position - hero.position).normalize()
        return MoveEvent(mob, offset, moveWorld = false)
    }
}

/*
 * Builder for CowardMobStrategy.
 */
class CowardMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = CowardMobStrategy(mob, world)
}
