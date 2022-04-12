package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.InputManager
import kotlin.math.abs

val UP = KeyCode.KEY_W
val DOWN = KeyCode.KEY_S
val LEFT = KeyCode.KEY_A
val RIGHT = KeyCode.KEY_D

val INVENTORY = KeyCode.KEY_I

val SPELL_H = KeyCode.KEY_H
val SPELL_J = KeyCode.KEY_J
val SPELL_K = KeyCode.KEY_K
val SPELL_L = KeyCode.KEY_L
val SPELL_SUBMIT = KeyCode.SPACE

private val MOVE_KEYS = setOf(UP, DOWN, LEFT, RIGHT)
private val SPELL_KEYS = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_SUBMIT)

class PlayerController(
    private val hero: Hero,
    private val input: InputManager,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun codeUpDown(code: KeyCode?): Int {
        val result = when (code ?: input.peek()) {
            UP -> -1
            DOWN -> 1
            else -> null
        }
        if (code == null && result != null) {
            input.poll()
        }
        return result ?: 0
    }

    private fun codeLeftRight(code: KeyCode?): Int {
        val result = when (code ?: input.peek()) {
            LEFT -> -1
            RIGHT -> 1
            else -> null
        }
        if (code == null && result != null) {
            input.poll()
        }
        return result ?: 0
    }

    private fun handleMoveKey(code: KeyCode) {
        val (x, y) = when (code) {
            UP, DOWN -> codeLeftRight(null) to codeUpDown(code)
            LEFT, RIGHT -> codeLeftRight(code) to codeUpDown(null)
            else -> return
        }
        hero.direction = Position.create(x, y)
        if (abs(hero.direction.x) + abs(hero.direction.y) > 0) {
            val event = MoveEvent(hero, hero.direction, moveWorld = true)
            eventBus.fire(event)
        }
    }

    private fun handleSpellKey(code: KeyCode) {

    }

    override fun action() {
        input.poll()?.let { code ->
            println(code)
            when (code) {
                in MOVE_KEYS -> handleMoveKey(code)
                in SPELL_KEYS -> handleSpellKey(code)
                else -> return
            }
        }
    }

    companion object {
        val SUPPORTED_KEYS = MOVE_KEYS + SPELL_KEYS
    }
}
