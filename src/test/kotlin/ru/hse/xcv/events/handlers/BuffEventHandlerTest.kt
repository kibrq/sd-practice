package ru.hse.xcv.events.handlers

import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.hse.xcv.events.BuffEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.stats.Stats
import kotlin.test.assertEquals

internal class BuffEventHandlerTest {
    @Test
    fun `Test buff event handler`() {
        val stats = Stats(
            power = 1,
            armor = 5,
            maxHealth = 100
        )
        val statsCopy = stats.copy()
        val stats2 = Stats(
            power = 1123,
            armor = 544,
            maxHealth = 1010
        )

        val entity = mock<Entity> {
            on(it.stats).doReturn(stats)
        }
        val event = mock<BuffEvent> {
            on(it.entity).doReturn(entity)
            on(it.buff).doReturn(stats2)
        }

        val handler = BuffEventHandler(mock())
        handler.handle(event)
        statsCopy += stats2
        assertEquals(statsCopy, stats)
    }
}
