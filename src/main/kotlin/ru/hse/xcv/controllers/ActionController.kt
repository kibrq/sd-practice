package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus

abstract class ActionController {
    abstract val eventFactory: EventBus
    abstract fun action()
}
