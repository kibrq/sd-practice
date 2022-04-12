package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats
import kotlin.random.Random

abstract class Mob(
    position: Position,
    direction: Position,
    moveSpeed: Int,
    stats: Stats,
    val experienceGain: Int
) : Entity(position, direction, moveSpeed, stats) {

    companion object {
        fun getRandomMob(position: Position, direction: Position): Mob {
            return when (Random.nextInt(0, 3)) {
                0 -> Zombie(position, direction)
                1 -> Maxim(position, direction)
                2 -> Dragon(position, direction)
                else -> throw IllegalStateException()
            }
        }
    }
}
