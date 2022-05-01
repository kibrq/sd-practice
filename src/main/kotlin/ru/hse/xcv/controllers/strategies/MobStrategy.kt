package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.events.Event
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World

interface MobStrategy {
    val mob: Mob
    val world: World

    fun takeAction(): List<Event>
}

interface MobStrategyBuilder {
    fun build(mob: Mob, world: World): MobStrategy
}
