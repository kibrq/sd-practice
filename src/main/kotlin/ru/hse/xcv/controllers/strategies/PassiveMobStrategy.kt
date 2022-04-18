package ru.hse.xcv.controllers.strategies

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.Event
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World

class PassiveMobStrategy(
    override val mob: Mob,
    override val world: World
) : MobStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun takeAction(): Event? = null
}
