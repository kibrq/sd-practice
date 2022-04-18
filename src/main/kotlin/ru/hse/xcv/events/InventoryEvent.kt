package ru.hse.xcv.events

import ru.hse.xcv.view.State

sealed interface InventoryEvent : Event

data class ScrollInventoryEvent(
    val dPos: Int
) : InventoryEvent

data class SwitchScreenEvent(
    val newState: State.Type
) : InventoryEvent
