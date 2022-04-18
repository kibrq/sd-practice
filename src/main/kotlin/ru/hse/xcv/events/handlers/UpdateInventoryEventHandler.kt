package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.UpdateInventoryEvent
import ru.hse.xcv.view.InventoryItemList

class UpdateInventoryEventHandler(
    override val inventoryItemList: InventoryItemList
) : InventoryEventHandler<UpdateInventoryEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: UpdateInventoryEvent) {
        inventoryItemList.items = event.newInventory
        inventoryItemList.equippedItems = event.newEquippedItems
        // costyl
        inventoryItemList.scrollbar.incrementValues()
        inventoryItemList.scrollbar.decrementValues()
    }
}
