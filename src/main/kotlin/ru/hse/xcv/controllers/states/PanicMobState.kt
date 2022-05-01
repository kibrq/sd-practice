package ru.hse.xcv.controllers.states

import ru.hse.xcv.controllers.MobController
import ru.hse.xcv.controllers.strategies.CowardMobStrategy
import ru.hse.xcv.controllers.strategies.MobStrategy

class PanicMobState(override val strategy: MobStrategy) : MobState {
    private val cowardStrategy = CowardMobStrategy(strategy.mob, strategy.world)

    override fun takeAction() = cowardStrategy.takeAction()

    override fun handleMobHealth(controller: MobController) {
        val mob = strategy.mob
        if (mob.currentHealth > mob.panicHealthThreshold) {
            controller.state = NormalMobState(strategy)
        }
    }
}
