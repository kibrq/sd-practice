package ru.hse.xcv.events

import ru.hse.xcv.controllers.ActionController

class EventFactory(
    private val eventQueue: ArrayDeque<Event>,
) {
    fun event(type: Event, callback: ActionController?){

    }
}
