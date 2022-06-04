package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.controllers.states.MobState
import ru.hse.xcv.events.EventBus

/*
 * Decides an action for a mob based on its strategy.
 */
class MobController(
    var state: MobState,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Delegates taking an action to MobStrategy.
     */
    override fun action(): Boolean {
        state.handleMobHealth(this)
        state.takeAction().forEach {
            eventBus.fire(it)
        }
        return true
    }
}
