package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

/*
 * A mediocre aggressive mob.
 */
class Maxim(position: Position) : Mob(position) {
    override var moveSpeed = 20
    override val experienceGain = 40
    override var stats = Stats(
        power = 2,
        armor = 5,
        maxHealth = 50
    )
}
