package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CreateMobEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.entities.mobs.ReproducibleMob
import ru.hse.xcv.world.World
import kotlin.random.Random

/*
 * ReproducibleMobStrategy is a strategy of mob who can reproduce.
 */
class ReproducibleMobStrategy(
    override val mob: Mob,
    override val world: World,
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val reproducibleMob = mob as? ReproducibleMob

    /*
     * Reproduces in an adjacent tile.
     */
    override fun takeSingleAction() = reproducibleMob?.let {
        val (x, y) = it.position
        val newPosition = Position.create(x + Random.nextInt(-1, 1 + 1), y + Random.nextInt(-1, 1 + 1))
        CreateMobEvent(
            mob = it.clone(
                newPosition,
                Random.nextInt(it.reproduceCoolDown, it.reproduceCoolDown * 2 + 1)
            ),
            refer = it,
            coolDown = it.reproduceCoolDown
        )
    }
}

/*
 * Builder for ReproducibleMobStrategy.
 */
class ReproducibleMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World): MobStrategy = ReproducibleMobStrategy(mob, world)
}
