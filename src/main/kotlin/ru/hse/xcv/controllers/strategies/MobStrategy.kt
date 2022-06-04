package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World

/*
 * A mob strategy decides a mob action depending on current world.
 */
interface MobStrategy {
    val mob: Mob
    val world: World

    /*
     * Returns a list of Events corresponding to all the actions decided by this mob strategy.
     */
    fun takeAction(): List<Event>
}

/*
 * Builder for MobStrategy.
 */
interface MobStrategyBuilder {
    fun build(mob: Mob, world: World): MobStrategy
}
