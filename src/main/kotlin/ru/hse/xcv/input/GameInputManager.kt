package ru.hse.xcv.input

import org.hexworks.cobalt.logging.api.LoggerFactory

import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.LetterPressedEvent
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.State
import java.util.concurrent.ConcurrentSkipListSet

const val SPELL_KEY_BUFFER_SIZE = 10
const val READY_SPELLS_QUEUE_SIZE = 10
const val ZXC = "ZXC"

val WTF_Z = KeyCode.KEY_Z
val WTF_X = KeyCode.KEY_X
val WTF_C = KeyCode.KEY_C


val INVENTORY = KeyCode.KEY_I

val SPELL_H = KeyCode.KEY_H
val SPELL_J = KeyCode.KEY_J
val SPELL_K = KeyCode.KEY_K
val SPELL_L = KeyCode.KEY_L
val SPELL_CAST = KeyCode.SPACE

/*
 * Very complicated logic for smooth movement.
 */
class GameInputManager(override val eventBus: EventBus): InputManager {

    private val logger = LoggerFactory.getLogger(javaClass)

    var zxc: Boolean = false
        private set
    private val zxcQueue = ArrayDeque<Char>()

    private val movementKeysPressed = ConcurrentSkipListSet<KeyCode>()
    private val spellKeyBuffer = mutableListOf<KeyCode>()
    private val readySpellsQueue = ArrayDeque<String>()

    private val toRemove = mutableSetOf<KeyCode>()
    private val nextToAdd = mutableSetOf<KeyCode>()
    private var toAdd = setOf<KeyCode>()
    private var previousMovementKeys = mutableSetOf<KeyCode>()

    private fun fireLetterPressedEvent(code: KeyCode) {
        val event = LetterPressedEvent(code.toCharOrNull() ?: return)
        eventBus.fire(event)
    }

    private fun zxcKeyPressed(code: KeyCode) {
        if (zxcQueue.size == 3) zxcQueue.removeFirst()
        zxcQueue.add(code.toCharOrNull() ?: return)
    }

    private fun spellKeyPressed(code: KeyCode) {
        if (code == SPELL_CAST) {
            if (spellKeyBuffer.isNotEmpty() && readySpellsQueue.size < READY_SPELLS_QUEUE_SIZE) {
                val spell = spellKeyBuffer.mapNotNull { it.toCharOrNull() }.joinToString("")
                readySpellsQueue.add(spell)
            }
            spellKeyBuffer.clear()
            if (zxcQueue.joinToString("") == ZXC) {
                zxc = !zxc
                zxcQueue.clear()
            }
            fireLetterPressedEvent(code)
        } else if (spellKeyBuffer.size < SPELL_KEY_BUFFER_SIZE) {
            spellKeyBuffer.add(code)
            fireLetterPressedEvent(code)
        }
    }

    private fun movementKeyPressed(code: KeyCode) {
        if (code in toRemove) {
            nextToAdd.add(code)
        }
        logger.debug("Movement key pressed")
        movementKeysPressed.add(code)
        previousMovementKeys.remove(code)
    }

    val currentMovementKeys: Set<KeyCode>
        get() {
            previousMovementKeys = movementKeysPressed.toMutableSet()
            movementKeysPressed.removeAll(toRemove)
            toRemove.clear()
            previousMovementKeys.addAll(toAdd)
            toAdd = nextToAdd.toSet()
            nextToAdd.clear()
            return previousMovementKeys
        }

    val readySpell: String?
        get() = readySpellsQueue.removeFirstOrNull()

    override fun keyPressed(code: KeyCode) {
        when (code) {
            in MOVE_KEYS -> movementKeyPressed(code)
            in SPELL_KEYS -> spellKeyPressed(code)
            in ZXC_KEYS -> zxcKeyPressed(code)
            INVENTORY -> eventBus.fire(SwitchScreenEvent(State.Type.INVENTORY))
            else -> return
        }
    }

    override fun keyReleased(code: KeyCode) {
        if (code !in MOVE_KEYS) return

        if (code in previousMovementKeys) {
            movementKeysPressed.remove(code)
        } else {
            toRemove.add(code)
        }
    }

    companion object {
        private val ZXC_KEYS = setOf(WTF_Z, WTF_X, WTF_C)
        private val MOVE_KEYS = setOf(UP, DOWN, LEFT, RIGHT)
        private val SPELL_KEYS = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_CAST)
        val SUPPORTED_KEYS = ZXC_KEYS + MOVE_KEYS + SPELL_KEYS
    }
}
