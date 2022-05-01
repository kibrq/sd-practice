package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.events.Event
import ru.hse.xcv.world.World
import kotlin.reflect.KClass


class CompositeMobStrategy(
    override val mob: Mob,
    override val world: World,
    private val strategies: List<MobStrategy>
) : MobStrategy {    
    override fun takeAction(): List<Event> {
        return strategies.flatMap { strategy -> strategy.takeAction() }
    }
}

class CompositeMobStrategyBuilder(
    private val builders: List<MobStrategyBuilder>
) : MobStrategyBuilder {
    constructor(vararg builders: MobStrategyBuilder) : this(builders.asList()) {}

    override fun build(mob: Mob, world: World) = CompositeMobStrategy(mob, world, builders.map { it.build(mob, world) }.toList())
}
