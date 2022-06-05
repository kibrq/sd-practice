package ru.hse.xcv.model.entities.mobs.cyberpunk

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.Microchel
import ru.hse.xcv.model.stats.Stats

/*
 * A weak coward mob.
 */
class CyberpunkMicrochel(position: Position) : Microchel(position) {
    override var moveSpeed = 10
    override val experienceGain = 35
    override val stats = Stats(
        power = 1,
        armor = 1,
        maxHealth = 20
    )
}
