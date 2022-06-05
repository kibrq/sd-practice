package ru.hse.xcv.events.handlers

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.world.World
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class HPChangeHandlerTest {
    @Test
    fun `Test HP change handler`() {
        var mobHP = 100
        var mobDeleted = false
        var experienceAdded = 0

        val entity = mock<Mob> {
            on(it.changeHP(any())).doAnswer { call ->
                mobHP += call.arguments[0] as Int
            }
            on(it.isDead)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true)
            on(it.experienceGain).doReturn(239)
        }
        val event = mock<HPChangeEvent> {
            on(it.entity).doReturn(entity)
            on(it.amount).doReturn(-40)
        }
        val experience = mock<Experience> {
            on(it.level).doReturn(0)
        }
        val hero = mock<Hero> {
            on(it.addExperience(any())).doAnswer { call ->
                experienceAdded += call.arguments[0] as Int
            }
            on(it.experience).doReturn(experience)
        }
        val world = mock<World> {
            on(it.deleteObject(any())).doAnswer {
                mobDeleted = true
            }
            on(it.hero).doReturn(hero)
        }

        val handler = HPChangeHandler(world, mock(), mock())

        handler.handle(event)
        assertEquals(60, mobHP)

        handler.handle(event)
        assertEquals(20, mobHP)

        handler.handle(event)
        assertTrue(mobDeleted)
        assertEquals(239, experienceAdded)
    }
}
