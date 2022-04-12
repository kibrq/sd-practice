package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
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
val SPELL_CAST = KeyCode.SPACE

private val MOVE_KEYS = setOf(UP, DOWN, LEFT, RIGHT)
private val SPELL_KEYS = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_CAST)

class PlayerController(
    private val hero: Hero,
    private val input: InputManager,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val lastSpellKeys = mutableListOf<KeyCode>()

    init {
        hero.spellBook.spells.add(FireballSpell())
        hero.spellBook.spells.add(ChainLightningSpell())
        hero.spellBook.spells.add(HealSpell())
    }

    private fun castSpell() {
        val combination = lastSpellKeys.map {
            when (it) {
                SPELL_H -> 'H'
                SPELL_J -> 'J'
                SPELL_K -> 'K'
                SPELL_L -> 'L'
                else -> return
            }
        }.joinToString("")
        val spell = hero.spellBook.search(combination) ?: return
        logger.debug("${spell.name} was casted!")
    }

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
        if (code !in SPELL_KEYS) return
        if (code == SPELL_CAST) {
            castSpell()
            lastSpellKeys.clear()
        } else {
            lastSpellKeys.add(code)
        }
    }

    override fun action() {
        input.poll()?.let { code ->
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
