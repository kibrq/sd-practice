package ru.hse.xcv.controllers

import ru.hse.xcv.controllers.states.NormalMobState
import ru.hse.xcv.controllers.strategies.*
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
    private fun normalMobController(strategy: MobStrategy) = MobController(NormalMobState(strategy), eventBus)

    fun create(obj: DynamicObject, world: World): ActionController {
        return when (obj) {
            is Dragon -> normalMobController(CompositeMobStrategyBuilder(
                    AttackMobStrategyBuilder(), 
                    AggressiveMobStrategyBuilder()
                ).build(obj, world)),
                eventBus
            )
            is Zombie -> normalMobController(
                CanBeConfusedMobStrategyBuilder(
                    CompositeMobStrategyBuilder(AttackMobStrategyBuilder(), PassiveMobStrategyBuilder())
                ).build(obj, world),
                eventBus
            )
            is Maxim -> normalMobController(
                CanBeConfusedMobStrategyBuilder(
                    CompositeMobStrategyBuilder(AttackMobStrategyBuilder(), AggressiveMobStrategyBuilder())
                ).build(obj, world),
                eventBus
            )
            is Microchel -> normalMobController(
                CanBeConfusedMobStrategyBuilder(CowardMobStrategyBuilder()).build(obj, world),
                eventBus
            )
            is PoisonousMold -> MobController(
                CompositeMobStrategyBuilder(
                    AttackMobStrategyBuilder(),
                    ReproducibleMobStrategyBuilder()
                ).build(obj, world),
                eventBus
            )
            is FireballSpell.Fireball -> FireballController(obj, world, eventBus)
            is Hero -> PlayerController(world, inputManager, eventBus)
            else -> throw IllegalStateException()
        }
    }
}
