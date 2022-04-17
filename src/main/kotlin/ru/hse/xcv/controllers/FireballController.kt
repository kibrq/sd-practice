package ru.hse.xcv.controllers

import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
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

        val newPosition = fireball.position + offset
        return if (world.isEmpty(newPosition)) {
            val event = MoveEvent(fireball, offset, moveWorld = false)
            eventBus.fire(event)
            true
        } else if (world.model.dynamicLayer[newPosition] is Hero) {
            val event = MoveEvent(fireball, offset, moveWorld = false, crazyMovements = true)
            eventBus.fire(event)
            true
        } else {
            val entity = world.model.dynamicLayer[newPosition] as? Entity
            entity?.let {
                val event = DamageEvent(it, fireball.damage)
                eventBus.fire(event)
            }
            false
        }
    }
}
