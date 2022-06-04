package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.GameEvent
import ru.hse.xcv.events.InventoryEvent
import ru.hse.xcv.view.InventoryItemList
import ru.hse.xcv.world.World

/*
 * Handles events of type T.
 */
interface EventHandler<T : Event> {
    /*
     * Handles specified `event`.
     */
    fun handle(event: T)
}

/*
 * Handles GameEvent's of type T.
 */
interface GameEventHandler<T : GameEvent> : EventHandler<T> {
    val world: World
}

/*
 * Handles InventoryEvent's of type T.
 */
interface InventoryEventHandler<T : InventoryEvent> : EventHandler<T> {
    val inventoryItemList: InventoryItemList
}
