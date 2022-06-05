package ru.hse.xcv.controllers

import ru.hse.xcv.controllers.states.NormalMobState
import ru.hse.xcv.controllers.strategies.*
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.GameInputManager
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.*
import ru.hse.xcv.model.entities.mobs.Dragon
import ru.hse.xcv.model.entities.mobs.Maxim
import ru.hse.xcv.model.entities.mobs.Microchel
import ru.hse.xcv.model.entities.mobs.PoisonousMold
import ru.hse.xcv.model.entities.mobs.Zombie
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.world.World

/*
 * ActionController decides an action for a dynamic object.
 */
interface ActionController {
    val eventBus: EventBus

    /*
     * Makes an action and return if the controller should continue to exist.
     */
    fun action(): Boolean
}

/*
 * Factory for ActionController.
 */
class ActionControllerFactory(
    private val eventBus: EventBus,
    private val inputManager: GameInputManager
) {
    private fun normalMobController(strategy: MobStrategy) = MobController(NormalMobState(strategy), eventBus)

    /*
     * Creates an ActionController for a given DynamicObject.
     */
    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> normalMobController(
                CompositeMobStrategyBuilder(
                    AttackMobStrategyBuilder(),
                    AggressiveMobStrategyBuilder()
                ).build(obj, world)
            )
            is Zombie -> normalMobController(
                CanBeConfusedMobStrategyBuilder(
                    CompositeMobStrategyBuilder(AttackMobStrategyBuilder(), PassiveMobStrategyBuilder())
                ).build(obj, world)
            )
            is Maxim -> normalMobController(
                CanBeConfusedMobStrategyBuilder(
                    CompositeMobStrategyBuilder(AttackMobStrategyBuilder(), AggressiveMobStrategyBuilder())
                ).build(obj, world)
            )
            is Microchel -> normalMobController(
                CanBeConfusedMobStrategyBuilder(CowardMobStrategyBuilder()).build(obj, world)
            )
            is PoisonousMold -> normalMobController(
                CompositeMobStrategyBuilder(
                    AttackMobStrategyBuilder(),
                    ReproducibleMobStrategyBuilder()
                ).build(obj, world)
            )
            is FireballSpell.Fireball -> FireballController(obj, world, eventBus)
            is Hero -> PlayerController(world, inputManager, eventBus)
            else -> throw IllegalStateException()
        }
    }
}
