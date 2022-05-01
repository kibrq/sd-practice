package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class PoisonousMold(
    override var position: Position,
    override val reproducityCooldown: Int = 10
): ReproducibleMob(position) {
    override var experienceGain = 1
    override var moveSpeed = 5000
    override var stats = Stats(
        power = 10,
        armor = 10,
        maxHealth = 200
    )
    override fun clone(newPosition: Position, reproducityCooldown: Int) = PoisonousMold(newPosition, reproducityCooldown)
}
