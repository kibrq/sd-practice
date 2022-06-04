package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event

/*
 * A mob strategy with a single action.
 */
interface SingleEventMobStrategy : MobStrategy {
    /*
     * Returns an Event or null corresponding to an action decided by this mob strategy.
     */
    fun takeSingleAction(): Event?

    override fun takeAction(): List<Event> {
        val event = takeSingleAction() ?: return emptyList()
        return listOf(event)
    }
}
