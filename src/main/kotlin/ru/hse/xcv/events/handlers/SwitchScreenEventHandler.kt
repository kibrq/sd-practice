package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.MainScreen
import ru.hse.xcv.view.State

/*
 * Handles BuffEvent.
 */
class SwitchScreenEventHandler(
    private val window: MainScreen,
    private val states: List<State>
) : EventHandler<SwitchScreenEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Switches main screen to `event.newState`.
     */
    override fun handle(event: SwitchScreenEvent) {
        val state = states.firstOrNull { it.type == event.newState } ?: return
        window.screen.detachAllComponents()
        window.screen.addComponent(state.component)
        window.input = state.input
        logger.debug("Handle Switch screen!")
    }
}
