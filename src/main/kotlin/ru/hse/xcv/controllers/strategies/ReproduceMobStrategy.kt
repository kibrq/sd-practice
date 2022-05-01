package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import kotlin.random.Random

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.CreateMobEvent
import ru.hse.xcv.model.entities.ReproducibleMob
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World


class ReproducibleMobStrategy(
    override val mob: Mob,
    override val world: World,
): SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun takeSingleAction() : Event? {
        if (mob is ReproducibleMob) {
            val (x, y) = mob.position
            val newPosition = Position.create(x + Random.nextInt(-1, 1 + 1), y + Random.nextInt(-1, 1 + 1))
            return CreateMobEvent(
                mob = mob.clone(
                    newPosition,
                    Random.nextInt(mob.reproducityCooldown, mob.reproducityCooldown * 2 + 2)
                ),
                refer = mob,
                cooldown = mob.reproducityCooldown
            )
        }
        return null
    }
}

class ReproducibleMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = ReproducibleMobStrategy(mob, world)
}
