package ru.hse.xcv.model.stats

import kotlin.math.max

class Stats(
    var power: Int,
    var armor: Int,
    var currentHealth: Int,
    var maxHealth: Int,
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
