package ru.hse.xcv.events

import kotlin.collections.ArrayDeque

import ru.hse.xcv.events.Event

class EventFactory(
    private val eventQueue: ArrayDeque<Event>,
) {
}
