package ru.hse.xcv.events

import org.hexworks.cobalt.logging.api.LoggerFactory

import ru.hse.xcv.view.State
import ru.hse.xcv.view.MainScreen
import ru.hse.xcv.events.handlers.*
import ru.hse.xcv.view.PanelControllers
import ru.hse.xcv.world.World
import ru.hse.xcv.view.InventoryItemList

class EventBus {
    private val move = EventDispatcher<MoveEvent>()
    private val buff = EventDispatcher<BuffEvent>()
    private val createSpell = EventDispatcher<CastSpellEvent>()
    private val changeHP = EventDispatcher<HPChangeEvent>()
    private val letterPressed = EventDispatcher<LetterPressedEvent>()

    private val screenSwitch = EventDispatcher<SwitchScreenEvent>()
    private val scrollInventory= EventDispatcher<ScrollInventoryEvent>()

    private val logger = LoggerFactory.getLogger(javaClass)

    fun fire(event: Event) {
        when (event) {
            is MoveEvent -> move.run(event)
            is BuffEvent -> buff.run(event)
            is CastSpellEvent -> createSpell.run(event)
            is HPChangeEvent -> changeHP.run(event)
            is LetterPressedEvent -> letterPressed.run(event)
            is SwitchScreenEvent -> screenSwitch.run(event)
            is ScrollInventoryEvent -> scrollInventory.run(event)
        }
    }

    fun registerGameHandlers(world: World, panelControllers: PanelControllers) {
        move.register(MoveEventHandler(world))
        buff.register(BuffEventHandler(world))
        createSpell.register(CastSpellEventHandler(world, this))
        changeHP.register(
            HPChangeHandler(
                world,
                panelControllers.healthPanelController,
                panelControllers.levelPanelController
            )
        )
        letterPressed.register(LetterPressedEventHandler(world))
    }

    fun registerScreenEventHandlers(screen: MainScreen, states: List<State>) {
        screenSwitch.register(SwitchScreenEventHandler(screen, states))
    }

    fun registerInventoryEventHandlers(inventory: InventoryItemList) {
        scrollInventory.register(ScrollInventoryEventHandler(inventory))
    }
}
