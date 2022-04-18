package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.EquipItemEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.view.InventoryItemList

class EquipItemEventHandler(
    val hero: Hero,
    override val inventoryItemList: InventoryItemList
) : InventoryEventHandler<EquipItemEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: EquipItemEvent) {
        if (event.isEquip) {
            hero.equipItem(event.item)
        } else {
            hero.unequipItem(event.item)
        }
        // costyl
        inventoryItemList.scrollbar.incrementValues()
        inventoryItemList.scrollbar.decrementValues()
    }
}
