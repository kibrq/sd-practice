package ru.hse.xcv.controllers


import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.strategies.MobStrategy

class MobController(
    private val strategy: MobStrategy,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun action(): Boolean {
        strategy.takeAction()?.let {
            eventBus.fire(it)
        }
        return true
    }
}
