package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.isAdjacentDirection
import ru.hse.xcv.world.World

/*
 * AggressiveMobStrategy is a strategy of a passive mob.
 */
class PassiveMobStrategy(
    override val mob: Mob,
    override val world: World
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Attacks hero only on an adjacent tile.
     */
    override fun takeAction(): Event? {
        val hero = mob.findHero(world) ?: return null
        val offset = hero.position - mob.position
        return if (offset.isAdjacentDirection) {
            HPChangeEvent.createDamageEvent(hero, mob.damage)
        } else {
            null
        }
    }
}
