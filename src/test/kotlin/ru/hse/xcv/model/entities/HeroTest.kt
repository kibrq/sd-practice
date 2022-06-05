package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class HeroTest {
    private fun getHero() = Hero(Position.defaultPosition())

    @Test
    fun `Test add experience`() {
        val hero = getHero()
        hero.addExperience(50)
        assertEquals(3, hero.stats.power)
        assertEquals(3 * 5, hero.damage)
        assertEquals(3, hero.stats.armor)
        assertEquals(100, hero.stats.currentHealth)
        assertEquals(100, hero.stats.maxHealth)
        hero.addExperience(50)
        assertEquals(4, hero.stats.power)
        assertEquals(4 * 5, hero.damage)
        assertEquals(4, hero.stats.armor)
        assertEquals(120, hero.stats.currentHealth)
        assertEquals(120, hero.stats.maxHealth)
        hero.changeHP(-100)
    }

    @Test
    fun `Test deal damage`() {
        val hero = getHero()
        assertEquals(false, hero.isDead)
        hero.changeHP(-hero.stats.currentHealth)
        assertEquals(true, hero.isDead)
    }
}
