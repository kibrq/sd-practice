package ru.hse.xcv.input

import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus

val UP = KeyCode.KEY_W
val DOWN = KeyCode.KEY_S
val LEFT = KeyCode.KEY_A
val RIGHT = KeyCode.KEY_D

/*
 * An interface for a manager of pressed/released keys.
 */
interface InputManager {
    val eventBus: EventBus

    fun keyPressed(code: KeyCode)
    fun keyReleased(code: KeyCode)
}

/*
 * By default, nothing happens when any key is pressed.
 */
class DefaultInputManager(
    override val eventBus: EventBus
) : InputManager {
    override fun keyPressed(code: KeyCode) = Unit
    override fun keyReleased(code: KeyCode) = Unit
}
