package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.GameEvent
import ru.hse.xcv.events.InventoryEvent
import ru.hse.xcv.view.InventoryItemList
import ru.hse.xcv.world.World

interface EventHandler<T : Event> {
    fun handle(event: T)
}

interface GameEventHandler<T : GameEvent> : EventHandler<T> {
    val world: World
}

interface InventoryEventHandler<T : InventoryEvent> : EventHandler<T> {
    val inventoryItemList: InventoryItemList
}
