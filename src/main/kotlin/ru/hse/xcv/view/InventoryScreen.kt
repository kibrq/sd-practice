package ru.hse.xcv.view

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentContainer
import org.hexworks.zircon.api.component.ScrollBar
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.InventoryInputManager
import ru.hse.xcv.model.items.Helmet
import ru.hse.xcv.model.items.Item

class InventoryState(
    override val component: Component,
    override val input: InventoryInputManager
) : State {
    override val type = State.Type.INVENTORY
}

data class InventoryItemList(
    val items: List<Item>,
    val itemsRootPanel: ComponentContainer,
    val scrollbar: ScrollBar
)

data class CreateInventoryScreenReturn(
    val state: InventoryState,
    val itemList: InventoryItemList
)

fun itemToComponent(item: Item) =
    Components.label()
        .withPreferredSize(10, 2)
        .withText(item.name)
        .build()

fun createInventoryScreen(appConfig: AppConfig, eventBus: EventBus): CreateInventoryScreenReturn {
    val (width, height) = appConfig.size

    val itemsRootPanel = Components.panel()
        .withPreferredSize(width, height)
        .build()

    val items = (0..10).map { Helmet().apply { name = "Helmet${it}" } }.toMutableList()
    val scrollbar = Components.verticalScrollbar().withItemsShownAtOnce(3).build()

    scrollbar.onValueChange { event ->
        itemsRootPanel.detachAllComponents()
        val vbox = Components.vbox()
            .withPreferredSize(width, height)
            .build()
        val end = minOf(items.size, event.newValue + scrollbar.itemsShownAtOnce)
        items.subList(event.newValue, end)
            .map { itemToComponent(it) }
            .forEach { panel -> vbox.addComponent(panel) }
        itemsRootPanel.addComponent(vbox)
    }

    scrollbar.incrementValues()
    scrollbar.decrementValues()

    return CreateInventoryScreenReturn(
        InventoryState(
            component = itemsRootPanel,
            input = InventoryInputManager(eventBus)
        ),
        InventoryItemList(
            items = items,
            itemsRootPanel = itemsRootPanel,
            scrollbar = scrollbar
        )
    )
}
