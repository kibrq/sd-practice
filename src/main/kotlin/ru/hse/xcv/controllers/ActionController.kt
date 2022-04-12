package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.NoneEvent

import ru.hse.xcv.util.InputManager

import ru.hse.xcv.world.World

import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.Dragon
import ru.hse.xcv.model.Maxim
import ru.hse.xcv.model.Zombie
import ru.hse.xcv.model.Hero
import ru.hse.xcv.model.Mob

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
            is Mob -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Dragon -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Zombie -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Maxim  -> MobController(AggressiveMobStrategy(obj, world), eventFactory)
            is Hero   -> PlayerController(obj, inputManager, eventFactory)
        }
    }
}
