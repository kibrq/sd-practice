package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.MainScreen
import ru.hse.xcv.view.State

class SwitchScreenEventHandler(
    private val window: MainScreen,
    private val states: List<State>
) : EventHandler<SwitchScreenEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: SwitchScreenEvent) {
        val (newType) = event
        val state = states.firstOrNull { it.type == newType } ?: return
        window.screen.detachAllComponents()
        window.screen.addComponent(state.component)
        window.input = state.input
        logger.debug("Handle Switch screen!")
    }
}
