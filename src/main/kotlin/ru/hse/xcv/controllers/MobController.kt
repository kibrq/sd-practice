package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.controllers.strategies.MobStrategy
import ru.hse.xcv.events.EventBus

/*
 * Decides an action for a mob based on its strategy.
 */
class MobController(
    private val strategy: MobStrategy,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Delegates taking an action to MobStrategy.
     */
    override fun action(): Boolean {
        strategy.takeAction()?.let {
            eventBus.fire(it)
        }
        return true
    }
}
