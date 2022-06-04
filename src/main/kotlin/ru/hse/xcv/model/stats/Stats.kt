package ru.hse.xcv.model.stats

/*
 * Encapsulates entity's statsL power, armor, maxHP and current HP.
 */
data class Stats(
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
        require(maxHealth >= currentHealth)
    }

    /*
     * Is HP == 0.
     */
    val isDead
        get() = currentHealth == 0

    /*
     * Change HP by `amount`.
     */
    fun changeHP(amount: Int) {
        currentHealth = minOf(maxHealth, maxOf(0, currentHealth + amount))
    }

    operator fun plusAssign(other: Stats) {
        power = maxOf(0, power + other.power)
        armor = maxOf(0, armor + other.armor)
        maxHealth = maxOf(0, maxHealth + other.maxHealth)
        currentHealth = minOf(maxHealth, maxOf(0, currentHealth + other.currentHealth))
    }

    operator fun minusAssign(other: Stats) {
        power = maxOf(0, power - other.power)
        armor = maxOf(0, armor - other.armor)
        maxHealth = maxOf(0, maxHealth - other.maxHealth)
        currentHealth = minOf(maxHealth, maxOf(0, currentHealth - other.currentHealth))
    }

    operator fun times(multiplier: Int): Stats {
        require(multiplier >= 0)
        power *= multiplier
        armor *= multiplier
        currentHealth *= multiplier
        maxHealth *= multiplier
        return this
    }
}
