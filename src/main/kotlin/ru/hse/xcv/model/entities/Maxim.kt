package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class Maxim(
    position: Position,
    direction: Position
): Mob(position, direction, moveSpeed, stats, experienceGain) {
    companion object {
        const val moveSpeed = 4000
        const val experienceGain = 40000
        val stats = Stats(power = 2000, armor = 5000, maxHealth = 50000)
    }
}
