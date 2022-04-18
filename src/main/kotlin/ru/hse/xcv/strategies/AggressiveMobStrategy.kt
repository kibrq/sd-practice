package ru.hse.xcv.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

class AggressiveMobStrategy(
    override val mob: Mob,
    override val world: World
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(): Event? {
        val hero = world.nearestVisibleObjectInRectangle(mob.position, mob.fieldOfView, Hero::class) ?: return null
        val offset = (hero.position - mob.position).normalize()
        val newPosition = mob.position + offset
        val entity = world.getDynamicLayer(newPosition)
        return if (entity is Hero) {
            HPChangeEvent.createDamageEvent(entity, mob.damage)
        } else {
            MoveEvent(mob, offset, moveWorld = false)
        }
    }
}
