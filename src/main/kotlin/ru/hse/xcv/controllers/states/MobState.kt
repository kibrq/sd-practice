package ru.hse.xcv.controllers.states

import ru.hse.xcv.controllers.MobController
import ru.hse.xcv.controllers.strategies.MobStrategy
import ru.hse.xcv.events.Event

/*
 * State of the mob with a specified strategy.
 */
interface MobState {
    val strategy: MobStrategy

    /*
     * Returns a list of Events corresponding to all the actions decided by this mob strategy.
     */
    fun takeAction(): List<Event> = strategy.takeAction()

    /*
     * Handles mob health and may change its state.
     */
    fun handleMobHealth(controller: MobController)
}
