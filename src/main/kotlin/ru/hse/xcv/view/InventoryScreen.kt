package ru.hse.xcv.view

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ScrollBar
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.hse.xcv.events.EquipItemEvent
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.InventoryInputManager
import ru.hse.xcv.model.items.Item
import kotlin.math.min

/*
 * Inventory state of the game.
 */
class InventoryState(
    override val component: Component,
    override val input: InventoryInputManager
) : State {
    override val type = State.Type.INVENTORY
}

data class InventoryItemList(
    var items: List<Item>,
    var equippedItems: List<Item>,
    val scrollbar: ScrollBar
)

/*
 * Create a zircon Component from an item which is equipped/unequipped when status is true/false.
 */
fun itemToComponent(item: Item, status: Boolean, eventBus: EventBus): Component {
    val itemPanel = Components.panel()
        .withPreferredSize(30, 4)
        .build()

    val itemName = Components.label()
        .withPreferredSize(min(15, item.name.length), 4)
        .withText(item.name)
        .withPosition(0, 0)
        .build()

    val buttonText = if (status) "Equipped" else "Unequipped"
    val itemsStatus = Components.button()
        .withPreferredSize(buttonText.length + 2, 1)
        .withText(buttonText)
        .withPosition(15, 0)
        .build()

    itemsStatus.handleComponentEvents(ComponentEventType.ACTIVATED) {
        eventBus.fire(EquipItemEvent(item, !status))
        Processed
    }

    itemPanel.addComponent(itemName)
    itemPanel.addComponent(itemsStatus)

    return itemPanel
}

/*
 * Create inventory screen.
 */
fun createInventoryScreen(appConfig: AppConfig, eventBus: EventBus): Pair<InventoryState, InventoryItemList> {
    val (width, height) = appConfig.size

    val itemsRootPanel = Components.panel()
        .withPreferredSize(width, height)
        .build()

    val scrollbar = Components.verticalScrollbar().withItemsShownAtOnce(5).build()
    val inventoryItemList = InventoryItemList(
        items = emptyList(),
        equippedItems = emptyList(),
        scrollbar = scrollbar
    )
    val inventoryState = InventoryState(
        component = itemsRootPanel,
        input = InventoryInputManager(eventBus)
    )

    scrollbar.onValueChange { event ->
        itemsRootPanel.detachAllComponents()
        val vbox = Components.vbox()
            .withPreferredSize(width, height)
            .build()
        val items = inventoryItemList.items.toList()
        val equippedItems = inventoryItemList.equippedItems.toList()

        val end = minOf(items.size, event.newValue + scrollbar.itemsShownAtOnce)
        items.subList(event.newValue, end)
            .map { itemToComponent(it, it in equippedItems, eventBus) }
            .forEach { panel -> vbox.addComponent(panel) }
        itemsRootPanel.addComponent(vbox)
    }

    return inventoryState to inventoryItemList
}
