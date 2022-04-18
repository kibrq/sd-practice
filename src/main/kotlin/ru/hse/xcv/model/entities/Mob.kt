package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import kotlin.random.Random

abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int

    companion object {
        fun getRandomMob(position: Position) =
            when (Random.nextInt(0, 3)) {
                0 -> Zombie(position)
                1 -> Maxim(position)
                2 -> Dragon(position)
                else -> throw IllegalStateException()
            }
    }
}
