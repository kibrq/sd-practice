package ru.hse.xcv.model.entities

import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.util.Coordinate
import kotlin.math.max

open class Entity(
    position: Coordinate,
    direction: Coordinate,
    moveSpeed: Int,
    var stats: Stats
) : DynamicObject(position, direction, moveSpeed) {

    fun receiveDamage(damage: Int) {
        stats.currentHealth = max(0, stats.currentHealth - damage)
    }
}
