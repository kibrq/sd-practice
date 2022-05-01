package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.controllers.states.MobState
import ru.hse.xcv.events.EventBus

class MobController(
    var state: MobState,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun action(): Boolean {
        state.handleMobHealth(this)
        strategy.takeAction().forEach { eventBus.fire(it) }
        return true
    }
}
