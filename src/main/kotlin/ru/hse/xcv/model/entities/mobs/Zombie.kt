package ru.hse.xcv.model.entities.mobs

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.stats.Stats

/*
 * A mediocre passive mob.
 */
abstract class Zombie(position: Position) : Mob(position) {
    override var moveSpeed = 15
    override val experienceGain = 30
    override val stats = Stats(
        power = 2,
        armor = 2,
        maxHealth = 25
    )
}
