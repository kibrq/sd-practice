package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.UpdateInventoryEvent
import ru.hse.xcv.view.InventoryItemList

/*
 * Handles UpdateInventoryEvent.
 */
class UpdateInventoryEventHandler(
    override val inventoryItemList: InventoryItemList
) : InventoryEventHandler<UpdateInventoryEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Updates InventoryItemList with specified new items and equipped items.
     */
    override fun handle(event: UpdateInventoryEvent) {
        inventoryItemList.items = event.newInventory
        inventoryItemList.equippedItems = event.newEquippedItems
        // costyl
        inventoryItemList.scrollbar.incrementValues()
        inventoryItemList.scrollbar.decrementValues()
    }
}
