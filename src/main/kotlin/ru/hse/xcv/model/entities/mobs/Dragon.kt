package ru.hse.xcv.model.entities.mobs

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.stats.Stats

/*
 * A powerful aggressive mob that cannot be confused.
 */
abstract class Dragon(position: Position) : Mob(position) {
    override var moveSpeed = 10
    override var experienceGain = 200
    override val stats = Stats(
        power = 10,
        armor = 10,
        maxHealth = 200
    )
}
