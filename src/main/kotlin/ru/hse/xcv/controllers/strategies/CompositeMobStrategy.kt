package ru.hse.xcv.controllers.strategies

import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World

class CompositeMobStrategy(
    override val mob: Mob,
    override val world: World,
    private val strategies: List<MobStrategy>
) : MobStrategy {
    override fun takeAction() = strategies.flatMap { strategy -> strategy.takeAction() }
}

class CompositeMobStrategyBuilder(
    private val builders: List<MobStrategyBuilder>
) : MobStrategyBuilder {
    constructor(vararg builders: MobStrategyBuilder) : this(builders.asList())

    override fun build(mob: Mob, world: World): MobStrategy {
        val strategies = builders.map { it.build(mob, world) }
        return CompositeMobStrategy(mob, world, strategies)
    }
}
