package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.ScrollInventoryEvent
import ru.hse.xcv.view.InventoryItemList

class ScrollInventoryEventHandler(
    override val inventoryItemList: InventoryItemList
) : InventoryEventHandler<ScrollInventoryEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: ScrollInventoryEvent) {
        logger.debug("Handling scroll")

        val scrollbar = inventoryItemList.scrollbar
        if (event.dPos < 0) {
            scrollbar.decrementValues()
        } else if (event.dPos > 0 && scrollbar.currentValue < inventoryItemList.items.size) {
            scrollbar.incrementValues()
        }
    }
}
