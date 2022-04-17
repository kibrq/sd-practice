package ru.hse.xcv.strategies

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import kotlin.random.Random

class CanBeConfusedMobStrategy(private val strategy: MobStrategy) : MobStrategy {
    override val mob = strategy.mob
    override val world = strategy.world

    private val offsets = listOf(
        Position.create(1, 0),
        Position.create(-1, 0),
        Position.create(0, 1),
        Position.create(0, -1)
    )

    override fun takeAction(): Event? {
        return if (mob.isConfused.get() > 0) {
            val offset = offsets[Random.nextInt(4)]
            MoveEvent(mob, offset, moveWorld = false)
        } else {
            strategy.takeAction()
        }
    }
}
