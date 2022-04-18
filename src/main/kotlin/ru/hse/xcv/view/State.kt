package ru.hse.xcv.view

import org.hexworks.zircon.api.component.Component

import ru.hse.xcv.input.InputManager

interface State {
    val type: Type
    val component: Component
    val input: InputManager

    enum class Type {
        MENU, GAME, INVENTORY
    }
}
