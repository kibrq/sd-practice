package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

/*
 * A weak coward mob.
 */
class Microchel(position: Position) : Mob(position) {
    override var moveSpeed = 10
    override val experienceGain = 35
    override val stats = Stats(
        power = 1,
        armor = 1,
        maxHealth = 20
    )
}
