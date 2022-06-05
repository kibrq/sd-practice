package ru.hse.xcv.model.stats

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class StatsTest {
    @Test
    fun `Test stats creation with illegal args`() {
        assertFailsWith(IllegalArgumentException::class) {
            Stats(
                armor = -1,
                power = 1,
                maxHealth = 10
            )
        }

        assertFailsWith(IllegalArgumentException::class) {
            Stats(
                armor = 1,
                power = 1,
                currentHealth = 228,
                maxHealth = 10
            )
        }
    }

    @Test
    fun `Test plus stats`() {
        val stats1 = Stats(
            armor = 1,
            power = 1,
            maxHealth = 10
        )
        val stats2 = Stats(
            armor = 2,
            power = 2,
            maxHealth = 10
        )
        stats1 += stats2
        assertEquals(3, stats1.armor)
        assertEquals(3, stats1.power)
        assertEquals(20, stats1.maxHealth)
        assertEquals(20, stats1.currentHealth)
    }

    @Test
    fun `Test minus stats`() {
        val stats1 = Stats(
            armor = 3,
            power = 1,
            currentHealth = 8,
            maxHealth = 9
        )
        val stats2 = Stats(
            armor = 2,
            power = 2,
            currentHealth = 7,
            maxHealth = 10
        )
        stats1 -= stats2
        assertEquals(1, stats1.armor)
        assertEquals(0, stats1.power)
        assertEquals(0, stats1.maxHealth)
        assertEquals(0, stats1.currentHealth)
    }

    @Test
    fun `Test change HP`() {
        val stats = Stats(
            armor = 3,
            power = 1,
            currentHealth = 10,
            maxHealth = 10
        )
        stats.changeHP(-5)
        assertEquals(5, stats.currentHealth)
        stats.changeHP(-7)
        assertEquals(0, stats.currentHealth)
        stats.changeHP(228)
        assertEquals(10, stats.currentHealth)
    }

    @Test
    fun `Test times`() {
        var stats = Stats(
            armor = 3,
            power = 1,
            currentHealth = 10,
            maxHealth = 10
        )
        stats *= 4
        assertEquals(12, stats.armor)
        assertEquals(4, stats.power)
        assertEquals(40, stats.maxHealth)
        assertEquals(40, stats.currentHealth)
    }
}
