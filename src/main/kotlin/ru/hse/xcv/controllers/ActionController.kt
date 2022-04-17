package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.*
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.strategies.AggressiveMobStrategy
import ru.hse.xcv.strategies.CanBeConfusedMobStrategy
import ru.hse.xcv.strategies.CowardMobStrategy
import ru.hse.xcv.strategies.PassiveMobStrategy
import ru.hse.xcv.util.InputManager
import ru.hse.xcv.world.World

interface ActionController {
    val eventBus: EventBus
    fun action(): Boolean
}

class ActionControllerFactory(
    private val eventBus: EventBus,
    private val inputManager: InputManager,
) {
    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> MobController(AggressiveMobStrategy(obj, world), eventBus)
            is Zombie -> MobController(CanBeConfusedMobStrategy(PassiveMobStrategy(obj, world)), eventBus)
            is Maxim -> MobController(CanBeConfusedMobStrategy(AggressiveMobStrategy(obj, world)), eventBus)
            is Microchel -> MobController(CanBeConfusedMobStrategy(CowardMobStrategy(obj, world)), eventBus)
            is FireballSpell.Fireball -> FireballController(obj, world, eventBus)
            is Hero -> PlayerController(obj, inputManager, eventBus)
            else -> throw IllegalStateException()
        }
    }
}
