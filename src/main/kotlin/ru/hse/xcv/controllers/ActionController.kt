package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.NoneEvent

import ru.hse.xcv.util.InputManager

import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Dragon
import ru.hse.xcv.model.entities.Maxim
import ru.hse.xcv.model.entities.Zombie
import ru.hse.xcv.model.entities.Hero

interface ActionController {
    val eventFactory: EventBus
    fun action()
    fun start() {
        eventFactory.fire(NoneEvent(this))
    }
}

class ActionControllerFactory(
    private val eventFactory: EventBus,
    private val inputManager: InputManager,
) {
    fun create(obj: DynamicObject): ActionController {
        return when (obj) {
            is Dragon -> MobController(obj, MobStrategy(), eventFactory)
            is Zombie -> MobController(obj, MobStrategy(), eventFactory)
            is Maxim -> MobController(obj, MobStrategy(), eventFactory)
            is Hero -> PlayerController(obj, inputManager, eventFactory)
            else -> throw IllegalStateException()
        }
    }
}
