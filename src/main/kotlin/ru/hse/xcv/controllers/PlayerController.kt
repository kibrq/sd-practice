package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory

import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.data.Position

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent

import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.InputManager

import ru.hse.xcv.util.normalize

val UP = KeyCode.KEY_W
val DOWN = KeyCode.KEY_S
val LEFT = KeyCode.KEY_A
val RIGHT = KeyCode.KEY_D

val MOVEMENT = setOf(UP, DOWN, LEFT, RIGHT)

val INVENTORY = KeyCode.KEY_I

val SPELL_H = KeyCode.KEY_H
val SPELL_J = KeyCode.KEY_J
val SPELL_K = KeyCode.KEY_K
val SPELL_L = KeyCode.KEY_L
val SPELL_SUBMIT = KeyCode.SPACE

val SPELL_CASTING = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_SUBMIT)

class PlayerController(
    val hero: Hero,
    val input: InputManager,
    override val eventFactory: EventBus
) : ActionController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun action() {
        val pair = input.poll()
        if (pair != null) {
            val (pressed, code) = pair
            val direction = when(code) {
                UP    -> Position.create(0, -1 * pressed)
                DOWN  -> Position.create(0, 1 * pressed)
                LEFT  -> Position.create(pressed * -1, 0)
                RIGHT -> Position.create(pressed * 1, 0)
                else -> {Position.zero()}
            }
            val (newx, newy) = hero.direction + direction
            if (Math.abs(newx) <= 1 && Math.abs(newy) <= 1) {
                hero.direction = hero.direction + direction
            }
        }
        if (hero.direction != Position.zero())
            eventFactory.fire(MoveEvent(hero, hero.direction, true, this))
    }

    companion object {
        val SUPPORTED_KEYS = setOf(UP, DOWN, LEFT, RIGHT, SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_SUBMIT)
    }
}
