package ru.hse.xcv.controllers

import ru.hse.xcv.controllers.strategies.AggressiveMobStrategy
import ru.hse.xcv.controllers.strategies.CanBeConfusedMobStrategy
import ru.hse.xcv.controllers.strategies.CowardMobStrategy
import ru.hse.xcv.controllers.strategies.PassiveMobStrategy
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.GameInputManager
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.*
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.world.World

interface ActionController {
    val eventBus: EventBus
    fun action(): Boolean
}

class ActionControllerFactory(
    private val eventBus: EventBus,
    private val inputManager: GameInputManager
) {
    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> MobController(AggressiveMobStrategy(obj, world), eventBus)
            is Zombie -> MobController(CanBeConfusedMobStrategy(PassiveMobStrategy(obj, world)), eventBus)
            is Maxim -> MobController(CanBeConfusedMobStrategy(AggressiveMobStrategy(obj, world)), eventBus)
            is Microchel -> MobController(CanBeConfusedMobStrategy(CowardMobStrategy(obj, world)), eventBus)
            is FireballSpell.Fireball -> FireballController(obj, world, eventBus)
            is PickableItem -> PickableItemController(eventBus)
            is Hero -> PlayerController(world, inputManager, eventBus)
            else -> throw IllegalStateException()
        }
    }
}
