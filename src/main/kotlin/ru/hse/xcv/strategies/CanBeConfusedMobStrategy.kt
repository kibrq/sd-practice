package ru.hse.xcv.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.util.possibleDirections

class CanBeConfusedMobStrategy(private val strategy: MobStrategy) : MobStrategy {
    override val mob = strategy.mob
    override val world = strategy.world

    override fun takeAction(): Event? {
        return if (mob.isConfused.get() > 0) {
            val offset = possibleDirections.random()
            MoveEvent(mob, offset, moveWorld = false)
        } else {
            strategy.takeAction()
        }
    }
}
