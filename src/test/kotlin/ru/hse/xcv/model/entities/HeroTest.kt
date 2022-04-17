package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import org.junit.Test
import kotlin.test.assertEquals


class HeroTest {
    @Test
    fun `test add experience`() {
        val hero = Hero(Position.defaultPosition())
        hero.addExperience(50)
        assertEquals(3, hero.stats.power)
        assertEquals(3, hero.stats.armor)
        assertEquals(100, hero.stats.currentHealth)
        assertEquals(100, hero.stats.maxHealth)
        hero.addExperience(50)
        assertEquals(4, hero.stats.power)
        assertEquals(4, hero.stats.armor)
        assertEquals(120, hero.stats.currentHealth)
        assertEquals(120, hero.stats.maxHealth)
    }
}
