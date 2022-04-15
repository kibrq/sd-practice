package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats

sealed class Entity(
    position: Position,
    direction: Position,
    moveSpeed: Int,
    protected var stats: Stats
) : DynamicObject(position, direction, moveSpeed) {
    fun damage(amount: Int) = stats.damage(amount)

    fun heal(amount: Int) = stats.heal(amount)
}
