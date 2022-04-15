package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats

sealed class Entity(override var position: Position) : DynamicObject() {
    abstract var stats: Stats

    fun damage(amount: Int) = stats.damage(amount)
    fun heal(amount: Int) = stats.heal(amount)
}
