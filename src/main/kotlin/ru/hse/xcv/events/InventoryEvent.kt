package ru.hse.xcv.events

import ru.hse.xcv.model.items.Item
import ru.hse.xcv.view.State

/*
 * An interface representing any inventory event.
 */
sealed interface InventoryEvent : Event

/*
 * InventoryItemList should be updated with specified new items and equipped items.
 */
data class UpdateInventoryEvent(
    val newInventory: List<Item>,
    val newEquippedItems: List<Item>
) : InventoryEvent

/*
 * Equip `item`. Unequip if `isEquip` if false.
 */
data class EquipItemEvent(
    val item: Item,
    val isEquip: Boolean
) : InventoryEvent

/*
 * Scroll inventory by `dPos` sign.
 */
data class ScrollInventoryEvent(
    val dPos: Int
) : InventoryEvent

/*
 * Switch screen to `newState` screen.
 */
data class SwitchScreenEvent(
    val newState: State.Type
) : InventoryEvent
