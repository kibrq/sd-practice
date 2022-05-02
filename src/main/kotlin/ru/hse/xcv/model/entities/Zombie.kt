package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class Zombie(position: Position) : Mob(position) {
    override var moveSpeed = 15
    override val experienceGain = 30
    override val stats = Stats(
        power = 2,
        armor = 2,
        maxHealth = 25
    )
}
