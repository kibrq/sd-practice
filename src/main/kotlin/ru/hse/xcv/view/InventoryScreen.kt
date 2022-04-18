package ru.hse.xcv.view

import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ScrollBar
import org.hexworks.zircon.api.component.ComponentContainer
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.ProgressBar
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

import ru.hse.xcv.view.State
import ru.hse.xcv.input.InventoryInputManager
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.items.Helmet

class InventoryState(
    override val component: Component,
    override val input: InventoryInputManager,
) : State {
    override val type = State.Type.INVENTORY
}

data class InventoryItemList(
    val items: List<Item>,
    val itemsRootPanel: ComponentContainer,
    val scrollbar: ScrollBar,
)

data class CreateInventoryScreenReturn(
    val state: InventoryState,
    val itemList: InventoryItemList,
)


fun itemToPanel(item: Item): Component { 
    return Components.label()
        .withPreferredSize(10, 2)
        .withText(item.name)
        .build()
}


fun createInventoryScreen(appConfig: AppConfig, eventBus: EventBus): CreateInventoryScreenReturn {

    val (width, height) = appConfig.size

    val itemsRootPanel = Components.panel()
        .withPreferredSize(width, height)
        .build()
    
    val items = (0..10) . map {Helmet(name="Helmet${it}")} . toMutableList()
    val scrollbar = Components.verticalScrollbar().withItemsShownAtOnce(3).build()
    
    scrollbar.onValueChange { event ->
        itemsRootPanel.detachAllComponents()
        val vbox = Components.vbox()
            .withPreferredSize(width, height)
            .build()
        items.subList(event.newValue, minOf(items.size, event.newValue + scrollbar.itemsShownAtOnce))
            .map { itemToPanel(it) }
            .forEach { panel -> vbox.addComponent(panel) }
        itemsRootPanel.addComponent(vbox)
    }

    scrollbar.incrementValues()
    scrollbar.decrementValues()

    return CreateInventoryScreenReturn(
        InventoryState(
            component = itemsRootPanel,
            input = InventoryInputManager(eventBus),
        ),
        InventoryItemList(
            items = items,
            itemsRootPanel = itemsRootPanel,
            scrollbar = scrollbar
        )
    )
}
