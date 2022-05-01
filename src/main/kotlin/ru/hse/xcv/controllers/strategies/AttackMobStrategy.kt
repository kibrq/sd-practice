package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Rect
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

class AttackMobStrategy(
    override val mob: Mob,
    override val world: World
) : SingleEventMobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeSingleAction(): Event? {
        return mob.findHero(world)?.let {
            val (dx, dy) = it.position - mob.position
            return if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
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
