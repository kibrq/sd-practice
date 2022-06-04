package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World

/*
 * PassiveMobStrategy is a strategy of a passive mob.
 */
class PassiveMobStrategy(
    override val mob: Mob,
    override val world: World
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Does nothing.
     */
    override fun takeSingleAction() = null
}

/*
 * Builder for PassiveMobStrategy.
 */
class PassiveMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = PassiveMobStrategy(mob, world)
}
