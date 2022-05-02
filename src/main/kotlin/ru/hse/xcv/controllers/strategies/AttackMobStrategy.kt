package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World
import kotlin.math.abs

class AttackMobStrategy(
    override val mob: Mob,
    override val world: World
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeSingleAction(): Event? {
        return mob.findHero(world)?.let {
            val (dx, dy) = it.position - mob.position
            return if (abs(dx) <= 1 && abs(dy) <= 1) {
                HPChangeEvent.createDamageEvent(it, mob.damage)
            } else {
                null
            }
        }
    }
}

class AttackMobStrategyBuilder : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = AttackMobStrategy(mob, world)
}
