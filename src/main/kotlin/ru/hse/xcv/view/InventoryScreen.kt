package ru.hse.xcv.view

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ScrollBar
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.InventoryInputManager
import ru.hse.xcv.model.items.Item

class InventoryState(
    override val component: Component,
    override val input: InventoryInputManager
) : State {
    override val type = State.Type.INVENTORY
}

data class InventoryItemList(
    var items: List<Item>,
    val scrollbar: ScrollBar
)

fun itemToComponent(item: Item) =
    Components.label()
        .withPreferredSize(20, 4)
        .withText(item.name)
        .build()

fun createInventoryScreen(appConfig: AppConfig, eventBus: EventBus): Pair<InventoryState, InventoryItemList> {
    val (width, height) = appConfig.size

    val itemsRootPanel = Components.panel()
        .withPreferredSize(width, height)
        .build()

    val scrollbar = Components.verticalScrollbar().withItemsShownAtOnce(5).build()
    val inventoryItemList = InventoryItemList(
        items = emptyList(),
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
        val end = minOf(items.size, event.newValue + scrollbar.itemsShownAtOnce)
        items.subList(event.newValue, end)
            .map { itemToComponent(it) }
            .forEach { panel -> vbox.addComponent(panel) }
        itemsRootPanel.addComponent(vbox)
    }

    return inventoryState to inventoryItemList
}
