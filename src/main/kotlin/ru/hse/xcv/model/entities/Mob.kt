package ru.hse.xcv.model.entities

import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.util.Coordinate

class Mob(
    position: Coordinate,
    direction: Coordinate,
    moveSpeed: Int,
    stats: Stats,
    val experienceGain: Int
) : Entity(position, direction, moveSpeed, stats) {
}
