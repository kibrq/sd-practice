package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.world.World

/*
 * A hostile mob.
 */
abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int

    /*
     * Finds a hero if he is in mob's field of view.
     */
    fun findHero(world: World): Hero? = world.nearestVisibleObjectInRectangle(position, fieldOfView, Hero::class)

    companion object {
        private val allMobs = listOf<(Position) -> Mob>(
            { Dragon(it) },
            { Maxim(it) },
            { Zombie(it) },
            { Microchel(it) }
        )

        /*
         * Returns a random mob.
         */
        fun getRandomMob(position: Position) = allMobs.random().invoke(position)
    }
}
