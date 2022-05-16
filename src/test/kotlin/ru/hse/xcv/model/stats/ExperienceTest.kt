package ru.hse.xcv.model.stats

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ExperienceTest {
    @Test
    fun `test add experience`() {
        val exp = Experience()
        exp.applyExperience(50)
        assertEquals(50, exp.experience)
        assertEquals(1, exp.level)
        exp.applyExperience(50)
        assertEquals(0, exp.experience)
        assertEquals(2, exp.level)
        exp.applyExperience(1001)
        assertEquals(1, exp.experience)
        assertEquals(12, exp.level)
    }
}
