package ru.hse.xcv.input

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.ScrollInventoryEvent
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.State

val EQUIP = KeyCode.SPACE
val ESCAPE = KeyCode.ESCAPE

/*
 * InventoryInputManager handles user's pressed keys in inventory view.
 */
class InventoryInputManager(
    override val eventBus: EventBus
) : InputManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Handles `code` pressed.
     */
    override fun keyPressed(code: KeyCode) {
        logger.debug("Inventory manager key pressed")
        when (code) {
            UP -> eventBus.fire(ScrollInventoryEvent(-1))
            DOWN -> eventBus.fire(ScrollInventoryEvent(1))
            EQUIP -> {}
            ESCAPE, INVENTORY -> eventBus.fire(SwitchScreenEvent(State.Type.GAME))
            else -> return
        }
    }

    /*
     * I don't care.
     */
    override fun keyReleased(code: KeyCode) = Unit
}
