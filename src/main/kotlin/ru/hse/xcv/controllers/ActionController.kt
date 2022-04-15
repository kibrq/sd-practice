package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Dragon
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Maxim
import ru.hse.xcv.model.entities.Zombie
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.util.InputManager
import ru.hse.xcv.world.World

interface ActionController {
    val eventBus: EventBus
    fun action()
}

class ActionControllerFactory(
    private val eventBus: EventBus,
    private val inputManager: InputManager,
) {
    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> MobController(AggressiveMobStrategy(obj, world), eventBus)
            is Zombie -> MobController(AggressiveMobStrategy(obj, world), eventBus)
            is Maxim -> MobController(AggressiveMobStrategy(obj, world), eventBus)
            is FireballSpell.Fireball -> FireballController(obj, world, eventBus)
            is Hero -> PlayerController(obj, inputManager, eventBus)
            else -> throw IllegalStateException()
        }
    }
}
