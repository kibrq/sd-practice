package ru.hse.xcv.util

import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.LetterPressedEvent

const val SPELL_KEY_BUFFER_SIZE = 10
const val READY_SPELLS_QUEUE_SIZE = 10

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

class InputManager(private val eventBus: EventBus) {
    private val movementKeysPressed = mutableSetOf<KeyCode>()
    private val spellKeyBuffer = mutableListOf<KeyCode>()
    private val readySpellsQueue = ArrayDeque<String>()

    private fun fireLetterPressedEvent(code: KeyCode) {
        val event = LetterPressedEvent(code.toCharOrNull() ?: return)
        eventBus.fire(event)
    }

    private fun spellKeyPressed(code: KeyCode) {
        if (code == SPELL_CAST) {
            if (spellKeyBuffer.isNotEmpty() && readySpellsQueue.size < READY_SPELLS_QUEUE_SIZE) {
                val spell = spellKeyBuffer.mapNotNull { it.toCharOrNull() }.joinToString("")
                readySpellsQueue.add(spell)
            }
            spellKeyBuffer.clear()
            fireLetterPressedEvent(code)
        } else if (spellKeyBuffer.size < SPELL_KEY_BUFFER_SIZE) {
            spellKeyBuffer.add(code)
            fireLetterPressedEvent(code)
        }
    }

    val currentMovementKeys: Set<KeyCode>
        get() = movementKeysPressed.toSet()

    val readySpell: String?
        get() = readySpellsQueue.removeFirstOrNull()

    fun keyPressed(code: KeyCode) {
        when (code) {
            in MOVE_KEYS -> movementKeysPressed.add(code)
            in SPELL_KEYS -> spellKeyPressed(code)
            else -> return
        }
    }

    fun keyReleased(code: KeyCode) {
        if (code in MOVE_KEYS) {
            movementKeysPressed.remove(code)
        }
    }

    companion object {
        val MOVE_KEYS = setOf(UP, DOWN, LEFT, RIGHT)
        val SPELL_KEYS = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_CAST)
        val SUPPORTED_KEYS = MOVE_KEYS + SPELL_KEYS
    }
}
