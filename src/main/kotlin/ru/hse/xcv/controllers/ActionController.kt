package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventFactory

abstract class ActionController{
    abstract val eventFactory: EventFactory
    abstract fun action()
}
