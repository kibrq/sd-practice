package ru.hse.xcv.view

import org.hexworks.zircon.api.component.Component

import ru.hse.xcv.input.InputManager

/*
 * A state of the game.
 */
interface State {
    val type: Type
    val component: Component
    val input: InputManager

    /*
     * Type of game state.
     */
    enum class Type {
        MENU, GAME, INVENTORY
    }
}
