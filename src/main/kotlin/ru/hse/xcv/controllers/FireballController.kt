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
    private var mob: Mob? = null

    override fun action() {
        val rect = Size.create(20, 20)
        mob = mob ?: world.nearestObjectInNeighbourhood(fireball.position, rect, Mob::class)
        mob?.let {
            val dp = (it.position - fireball.position).normalize()
            val event = MoveEvent(fireball, dp, moveWorld = false)
            eventBus.fire(event)
        }
    }
}
