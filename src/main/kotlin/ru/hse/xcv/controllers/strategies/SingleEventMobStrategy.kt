package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event

interface SingleEventMobStrategy : MobStrategy {
    fun takeSingleAction(): Event?

    override fun takeAction(): List<Event> {
        val event = takeSingleAction() ?: return emptyList()
        return listOf(event)
    }
}
