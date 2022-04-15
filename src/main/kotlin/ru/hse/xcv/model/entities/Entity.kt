package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats
import kotlin.math.max

sealed class Entity(override var position: Position) : DynamicObject() {
    abstract var stats: Stats

    fun receiveDamage(damage: Int) {
        stats.currentHealth = max(0, stats.currentHealth - damage)
    }

    fun isDead() = stats.isDead
    fun damage(amount: Int) = stats.damage(amount)
    fun heal(amount: Int) = stats.heal(amount)
}
