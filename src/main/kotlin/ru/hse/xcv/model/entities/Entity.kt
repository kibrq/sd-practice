package ru.hse.xcv.model.entities

import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats
import org.hexworks.zircon.api.data.Position
import kotlin.math.max

open class Entity(
    position: Position,
    direction: Position,
    moveSpeed: Int,
    var stats: Stats
) : DynamicObject(position, direction, moveSpeed) {

    fun receiveDamage(damage: Int) {
        stats.currentHealth = max(0, stats.currentHealth - damage)
    }
}
