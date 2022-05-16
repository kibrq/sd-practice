package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position

abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int

    companion object {
        private val allMobs = listOf<(Position) -> Mob>(
            { Dragon(it) },
            { Maxim(it) },
            { Zombie(it) }
        )

        fun getRandomMob(position: Position) = allMobs.random().invoke(position)
    }
}
