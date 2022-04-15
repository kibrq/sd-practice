package ru.hse.xcv.controllers

import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

class FireballController(
    private val fireball: FireballSpell.Fireball,
    private val world: World,
    override val eventBus: EventBus
) : ActionController {
    private var myMob: Mob? = null

    override fun action(): Boolean {
        val rect = Size.create(20, 20)
        myMob = myMob ?: world.nearestObjectInNeighbourhood(fireball.position, rect, Mob::class)
        val offset = myMob?.let {
            (it.position - fireball.position).normalize()
        } ?: fireball.direction
        val event = MoveEvent(fireball, offset, moveWorld = false)
        eventBus.fire(event)
        return true
    }
}
