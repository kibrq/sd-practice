package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory

import ru.hse.xcv.events.ScrollInventoryEvent
import ru.hse.xcv.view.InventoryItemList

class ScrollInventoryEventHandler(
    override val inventoryItemList: InventoryItemList
): InventoryEventHandler<ScrollInventoryEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: ScrollInventoryEvent) {
        logger.debug("Handling")

        val (_, _, scrollbar) = inventoryItemList
        val (dPos) = event
        if (dPos < 0) {
            scrollbar.decrementValues()
        } else {
            scrollbar.incrementValues()
        }
    }
    
}
