package ru.hse.xcv.input

import org.hexworks.cobalt.logging.api.LoggerFactory

import org.hexworks.zircon.api.uievent.KeyCode

import ru.hse.xcv.events.ScrollInventoryEvent
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.State
import ru.hse.xcv.events.EventBus

class InventoryInputManager(
    override val eventBus: EventBus
): InputManager {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun keyPressed(code: KeyCode): Unit {
        logger.debug("INVENTORY MANAGER")
        when(code) {
            UP -> eventBus.fire(ScrollInventoryEvent(-1))
            DOWN -> eventBus.fire(ScrollInventoryEvent(1))
            KeyCode.SPACE -> {}
            KeyCode.ESCAPE -> eventBus.fire(SwitchScreenEvent(State.Type.GAME))
            else -> return
        }
    }

    override fun keyReleased(code: KeyCode) = Unit

    companion object {
        val SUPPORTED_KEYS = setOf(KeyCode.KEY_W, KeyCode.KEY_S, KeyCode.SPACE, KeyCode.ESCAPE)
    }
}
