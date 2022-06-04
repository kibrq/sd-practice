package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.possibleDirections
import ru.hse.xcv.world.World

/*
 * CanBeConfusedMobStrategy is a strategy of a mob that can be confused.
 */
class CanBeConfusedMobStrategy(
    override val mob: Mob,
    override val world: World,
    private val strategy: MobStrategy
) : MobStrategy {
    /*
     * If the mob is confused, move randomly. Otherwise, use provided strategy.
     */
    override fun takeAction(): List<Event> {
        return if (mob.isConfused.get() > 0) {
            val offset = possibleDirections.random()
            listOf(MoveEvent(mob, offset, moveWorld = false))
        } else {
            strategy.takeAction()
        }
    }
}

/*
 * Builder for CanBeConfusedMobStrategy.
 */
class CanBeConfusedMobStrategyBuilder(
    private val inner: MobStrategyBuilder
) : MobStrategyBuilder {
    override fun build(mob: Mob, world: World) = CanBeConfusedMobStrategy(mob, world, inner.build(mob, world))
}
