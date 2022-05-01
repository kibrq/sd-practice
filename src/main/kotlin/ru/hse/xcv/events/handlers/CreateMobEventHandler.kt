package ru.hse.xcv.events.handlers


import java.util.concurrent.TimeUnit

import org.hexworks.cobalt.logging.api.LoggerFactory
import ru.hse.xcv.events.CreateMobEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.world.World

class CreateMobEventHandler(
    override val world: World
) : GameEventHandler<CreateMobEvent> {
    private var lastHandled = mutableMapOf<Entity, Long>()

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: CreateMobEvent) {
        val currentTime = System.currentTimeMillis()

        val (mob, refer, cooldown) = event
        var needToCreate = 
            refer == null ||
            !lastHandled.containsKey(refer) ||
            currentTime - lastHandled[refer]!! >= TimeUnit.SECONDS.toMillis(cooldown.toLong())

        if (!needToCreate) {
            return
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
