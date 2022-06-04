package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.util.possibleDirections

/*
 * CanBeConfusedMobStrategy is a strategy of a mob that can be confused.
 */
class CanBeConfusedMobStrategy(private val strategy: MobStrategy) : MobStrategy {
    override val mob = strategy.mob
    override val world = strategy.world

    /*
     * If the mob is confused, move randomly. Otherwise, use provided strategy.
     */
    override fun takeAction(): Event? {
        return if (mob.isConfused.get() > 0) {
            val offset = possibleDirections.random()
            MoveEvent(mob, offset, moveWorld = false)
        } else {
            strategy.takeAction()
        }
    }
}
