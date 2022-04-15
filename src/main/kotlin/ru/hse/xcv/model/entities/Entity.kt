package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats

sealed class Entity(override var position: Position) : DynamicObject() {
    abstract var stats: Stats

    val isDead
        get() = stats.isDead

    fun changeHP(amount: Int) = stats.changeHP(amount)
}
