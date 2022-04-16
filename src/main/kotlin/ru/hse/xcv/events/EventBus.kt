package ru.hse.xcv.events

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.handlers.*
import ru.hse.xcv.view.PanelControllers
import ru.hse.xcv.world.World

class EventBus {
    private val none = EventDispatcher<NoneEvent>()
    private val move = EventDispatcher<MoveEvent>()
    private val buff = EventDispatcher<BuffEvent>()
    private val createSpell = EventDispatcher<CastSpellEvent>()
    private val changeHP = EventDispatcher<HPChangeEvent>()
    private val letterPressed = EventDispatcher<LetterPressedEvent>()

    private val logger = LoggerFactory.getLogger(javaClass)

    fun fire(event: Event) {
        when (event) {
            is NoneEvent -> none.run(event)
            is MoveEvent -> move.run(event)
            is BuffEvent -> buff.run(event)
            is CastSpellEvent -> createSpell.run(event)
            is HPChangeEvent -> changeHP.run(event)
            is LetterPressedEvent -> letterPressed.run(event)
        }
    }

    fun registerGameHandlers(world: World, panelControllers: PanelControllers) {
        none.register(object : EventHandler<NoneEvent> {
            override val world = world

            override fun handle(event: NoneEvent) = Unit
        })

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
}
