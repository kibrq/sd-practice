package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.EquipItemEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.view.InventoryItemList

/*
 * Handles EquipItemEvent.
 */
class EquipItemEventHandler(
    val hero: Hero,
    override val inventoryItemList: InventoryItemList
) : InventoryEventHandler<EquipItemEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Tries to equip/unequip hero with `event.item` and updates inventory view.
     */
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
