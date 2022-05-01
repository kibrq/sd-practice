package ru.hse.xcv.controllers.states

import ru.hse.xcv.controllers.MobController
import ru.hse.xcv.controllers.strategies.MobStrategy
import ru.hse.xcv.events.Event

interface MobState {
    val strategy: MobStrategy

    fun takeAction(): List<Event> = strategy.takeAction()

    fun handleMobHealth(controller: MobController)
}
