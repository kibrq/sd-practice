package ru.hse.xcv.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

class CowardMobStrategy(
    override val mob: Mob,
    override val world: World
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(): Event? {
        val hero = world.nearestVisibleObjectInRectangle(mob.position, mob.fieldOfView, Hero::class) ?: return null
        val offset = (mob.position - hero.position).normalize()
        return MoveEvent(mob, offset, moveWorld = false)
    }
}
