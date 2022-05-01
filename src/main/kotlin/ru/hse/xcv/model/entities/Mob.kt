package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.world.World

abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int
    open val panicHealthThreshold: Int
        get() = stats.maxHealth / 10

    fun findHero(world: World): Hero? = world.nearestVisibleObjectInRectangle(position, fieldOfView, Hero::class)

    companion object {
        private val allMobs = listOf<(Position) -> Mob>(
            { Dragon(it) },
            { Maxim(it) },
            { Zombie(it) },
            { Microchel(it) },
            { PoisonousMold(it) }
        )

        fun getRandomMob(position: Position) = allMobs.random().invoke(position)
    }
}
