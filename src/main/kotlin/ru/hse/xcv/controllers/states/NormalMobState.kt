package ru.hse.xcv.controllers.states

import ru.hse.xcv.controllers.MobController
import ru.hse.xcv.controllers.strategies.MobStrategy

/*
 * A MobState with normal health.
 */
class NormalMobState(override val strategy: MobStrategy) : MobState {
    override fun handleMobHealth(controller: MobController) {
        val mob = strategy.mob
        if (mob.currentHealth <= mob.panicHealthThreshold) {
            controller.state = PanicMobState(strategy)
        }
    }
}
