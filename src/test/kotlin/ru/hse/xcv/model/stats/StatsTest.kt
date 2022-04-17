package ru.hse.xcv.model.stats

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class StatsTest {

    @Test
    fun `test stats creation with illegal args`() {
        assertFailsWith(java.lang.IllegalArgumentException::class) {
            Stats(
                armor = -1,
                power = 1,
                maxHealth = 10
            )
        }

        assertFailsWith(java.lang.IllegalArgumentException::class) {
            Stats(
                armor = 1,
                power = 1,
                currentHealth = 228,
                maxHealth = 10
            )
        }
    }

    @Test
    fun `test plus stats`() {
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
    fun `test minus stats`() {
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
}
