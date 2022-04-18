package ru.hse.xcv.events.handlers


import org.hexworks.cobalt.logging.api.LoggerFactory

import ru.hse.xcv.view.MainScreen
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.view.State


class SwitchScreenEventHandler(
    val window: MainScreen,
    val states: List<State>,
) : EventHandler<SwitchScreenEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: SwitchScreenEvent) {
        val (newType) = event
        val state = states.filter { it.type == newType }.first()
        window.screen.detachAllComponents()
        window.screen.addComponent(state.component)
        window.input = state.input
        logger.debug("Handle Swithc screen!!")
    }
}
