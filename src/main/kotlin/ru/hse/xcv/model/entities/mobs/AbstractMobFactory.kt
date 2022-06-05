package ru.hse.xcv.model.entities.mobs

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.Mob

/*
 * Abstract factory for mobs.
 */
abstract class AbstractMobFactory {
    abstract fun createDragon(position: Position): Dragon
    abstract fun createMaxim(position: Position): Maxim
    abstract fun createMicrochel(position: Position): Microchel
    abstract fun createPoisonousMold(position: Position): PoisonousMold
    abstract fun createZombie(position: Position): Zombie

    private val allMobs = listOf<(Position) -> Mob>(
        { createDragon(it) },
        { createMaxim(it) },
        { createZombie(it) },
        { createMicrochel(it) },
        { createPoisonousMold(it) }
    )

    /*
     * Returns a random mob.
     */
    fun getRandomMob(position: Position) = allMobs.random().invoke(position)
}
