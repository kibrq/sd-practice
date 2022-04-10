package ru.hse.xcv.model.entities

import ru.hse.xcv.model.stats.Stats
import org.hexworks.zircon.api.data.Position

class Mob(
    position: Position,
    direction: Position,
    moveSpeed: Int,
    stats: Stats,
    val experienceGain: Int
) : Entity(position, direction, moveSpeed, stats) {
}
