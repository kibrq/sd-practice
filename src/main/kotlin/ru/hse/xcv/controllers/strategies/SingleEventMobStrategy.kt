package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.util.possibleDirections

interface SingleEventMobStrategy: MobStrategy {
    
    fun takeSingleAction(): Event?

    override fun takeAction() : List<Event> {
        val event = takeSingleAction()
        if (event == null) return listOf()
        return listOf(event)
    }
}
