package ru.hse.xcv.model.stats

import kotlin.math.max

class Stats(
    var power: Int = 0,
    var armor: Int = 0,
    var maxHealth: Int = 0,
    var currentHealth: Int = maxHealth
) {
    fun plus(other: Stats) {
        power = max(0, power + other.power)
        armor = max(0, armor + other.armor)
        currentHealth = max(0, currentHealth + other.currentHealth)
        maxHealth = max(0, maxHealth + other.maxHealth)
    }

    fun minus(other: Stats) {
        power = max(0, power - other.power)
        armor = max(0, armor - other.armor)
        currentHealth = max(0, currentHealth - other.currentHealth)
        maxHealth = max(0, maxHealth - other.maxHealth)
    }
}
