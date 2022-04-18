package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.world.World
import kotlin.random.Random

abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int

    fun findHero(world: World): Hero? = world.nearestVisibleObjectInRectangle(position, fieldOfView, Hero::class)

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
