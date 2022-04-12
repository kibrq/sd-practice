package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.stats.Stats

class Dragon(
    position: Position,
    direction: Position
) : Mob(position, direction, moveSpeed, stats, experienceGain) {
    companion object {
        const val moveSpeed = 10
        const val experienceGain = 200
        val stats = Stats(power = 10, armor = 10, maxHealth = 200)
    }
}
