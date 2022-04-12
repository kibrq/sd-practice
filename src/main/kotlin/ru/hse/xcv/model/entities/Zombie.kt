package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class Zombie(
    position: Position,
    direction: Position
): Mob(position, direction, moveSpeed, stats, experienceGain) {
    companion object {
        const val moveSpeed = 2
        const val experienceGain = 20
        val stats = Stats(power = 2, armor = 2, maxHealth = 20)
    }
}
