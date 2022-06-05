package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.world.World

/*
 * A hostile mob.
 */
abstract class Mob(position: Position) : Entity(position) {
    abstract val experienceGain: Int
    open val panicHealthThreshold: Int
        get() = stats.maxHealth / 10

    /*
     * Finds a hero if he is in mob's field of view.
     */
    fun findHero(world: World): Hero? = world.nearestVisibleObjectInRectangle(position, fieldOfView, Hero::class)
}
