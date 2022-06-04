package ru.hse.xcv.events.handlers

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.CreateMobEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.world.World
import java.util.concurrent.TimeUnit

/*
 * Handles CreateMobEvent.
 */
class CreateMobEventHandler(
    override val world: World
) : GameEventHandler<CreateMobEvent> {
    private val lastHandled = mutableMapOf<Entity, Long>()

    private val logger = LoggerFactory.getLogger(javaClass)

    /*
     * Creates `event.mob` is cool down has ended.
     */
    override fun handle(event: CreateMobEvent) {
        val (mob, refer, coolDown) = event
        val currentTime = System.currentTimeMillis()

        lastHandled[refer]?.let {
            if (currentTime - it < TimeUnit.SECONDS.toMillis(coolDown.toLong())) {
                return
            }
        }

        if (world.isEmpty(mob.position)) {
            world.createObject(mob, mob.position)
            refer?.let {
                lastHandled[it] = currentTime
            }
            lastHandled[mob] = currentTime
        }
    }
}
