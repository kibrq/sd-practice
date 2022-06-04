package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.stats.Stats
import java.util.concurrent.atomic.AtomicInteger

/*
 * A dynamic object with stats.
 */
sealed class Entity(override var position: Position) : DynamicObject() {
    abstract var stats: Stats

    var isConfused: AtomicInteger = AtomicInteger(0)

    val damage
        get() = stats.power * 5

    val isDead
        get() = stats.isDead

    /*
     * Change HP by `amount`.
     */
    fun changeHP(amount: Int) = stats.changeHP(amount)
}
