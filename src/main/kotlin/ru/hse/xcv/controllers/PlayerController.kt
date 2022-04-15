package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CastSpellEvent
import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.*
import kotlin.math.abs

class PlayerController(
    private val hero: Hero,
    private val input: InputManager,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun handleMovement() {
        val (x, y) = input.currentMovementKeys.map {
            when (it) {
                UP -> 0 to -1
                DOWN -> 0 to 1
                LEFT -> -1 to 0
                RIGHT -> 1 to 0
                else -> 0 to 0
            }
        }.sum()

        if (abs(x) + abs(y) > 0) {
            hero.direction = Position.create(x, y)
            val event = MoveEvent(hero, hero.direction, moveWorld = true)
            eventBus.fire(event)
        }
        if (x == 1) {
            val event = DamageEvent(hero, 2)
            eventBus.fire(event)
        }
    }

    private fun handleSpellCast() {
        val combination = input.readySpell ?: return
        val spell = hero.spellBook.search(combination) ?: return
        logger.debug("${spell.name} was casted!")
        val event = CastSpellEvent(spell, hero.position, hero.direction, hero.level)
        eventBus.fire(event)
    }

    override fun action(): Boolean {
        handleMovement()
        handleSpellCast()
        return true
    }
}
