package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class Maxim(
    position: Position,
    direction: Position
) : Mob(position, direction, moveSpeed, stats, experienceGain) {
    companion object {
        const val moveSpeed = 20
        const val experienceGain = 40
        val stats = Stats(power = 2, armor = 5, maxHealth = 50)
    }
}
