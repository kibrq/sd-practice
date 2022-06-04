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
     * Returns an Event or null corresponding to an action decided by this mob strategy.
     */
    fun takeAction(): Event?
}
