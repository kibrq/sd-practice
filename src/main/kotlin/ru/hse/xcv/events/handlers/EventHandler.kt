package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.world.World
import ru.hse.xcv.view.InventoryItemList

interface EventHandler<T : Event> {
    fun handle(event: T)
}

interface GameEventHandler<T : Event> : EventHandler<T> {
    val world: World
}

interface InventoryEventHandler<T: Event> : EventHandler<T> {
    val inventoryItemList: InventoryItemList
}
