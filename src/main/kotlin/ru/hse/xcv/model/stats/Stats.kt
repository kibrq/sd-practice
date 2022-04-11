package ru.hse.xcv.model.stats

import kotlin.math.max

class Stats(
    var power: Int = 0,
    var armor: Int = 0,
    var maxHealth: Int = 0,
    var currentHealth: Int = maxHealth
) {

    init {
        require(power >= 0)
        require(armor >= 0)
        require(maxHealth >= 0)
        require(currentHealth >= 0)
    }

    operator fun plusAssign(other: Stats) {
        power = max(0, power + other.power)
        armor = max(0, armor + other.armor)
        currentHealth = max(0, currentHealth + other.currentHealth)
        maxHealth = max(0, maxHealth + other.maxHealth)
    }

    operator fun minusAssign(other: Stats) {
        power = max(0, power - other.power)
        armor = max(0, armor - other.armor)
        currentHealth = max(0, currentHealth - other.currentHealth)
        maxHealth = max(0, maxHealth - other.maxHealth)
    }
}
