package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.util.normalize
import ru.hse.xcv.world.World

/*
 * Decides an action for a fireball.
 */
class FireballController(
    private val fireball: FireballSpell.Fireball,
    private val world: World,
    override val eventBus: EventBus
) : ActionController {
    private var myMob: Mob? = null

    /*
     * Moves to the nearest mob or damages it if adjacent.
     */
    override fun action(): Boolean {
        myMob = myMob ?: world.nearestVisibleObjectInRectangle(fireball.position, fireball.fieldOfView, Mob::class)
        val offset = myMob?.let {
            (it.position - fireball.position).normalize()
        } ?: fireball.direction

        val newPosition = fireball.position + offset
        return if (world.isEmpty(newPosition)) {
            val event = MoveEvent(fireball, offset, moveWorld = false)
            eventBus.fire(event)
            true
        } else if (world.getDynamicLayer(newPosition) is Hero) {
            val event = MoveEvent(fireball, offset, moveWorld = false, crazyMovements = true)
            eventBus.fire(event)
            true
        } else {
            val entity = world.getDynamicLayer(newPosition) as? Entity
            entity?.let {
                val event = HPChangeEvent.createDamageEvent(it, fireball.damage)
                eventBus.fire(event)
            }
            world.deleteObject(fireball)
            false
        }
    }
}
