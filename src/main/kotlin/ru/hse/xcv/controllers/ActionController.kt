package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus

import ru.hse.xcv.util.InputManager

import ru.hse.xcv.world.World

import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.*

interface ActionController {
    val eventFactory: EventBus
    fun action()
}

class ActionControllerFactory(
    private val eventFactory: EventBus,
    private val inputManager: InputManager,
) {
    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Zombie -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Maxim  -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Hero -> PlayerController(obj, inputManager, eventFactory)
            else -> throw IllegalStateException()
        }
    }
}
