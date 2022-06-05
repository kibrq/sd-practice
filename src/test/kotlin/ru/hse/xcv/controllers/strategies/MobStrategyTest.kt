package ru.hse.xcv.controllers.strategies

import org.hexworks.zircon.api.data.Position
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.events.LetterPressedEvent
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.normalize
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.test.*

internal class MobStrategyTest {
    @Test
    fun `Test aggressive mob strategy success`() {
        val heroPosition = Position.create(2, 1)
        val mobPosition = Position.create(0, 0)

        val hero = mock<Hero> {
            on(it.position).doReturn(heroPosition)
        }
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.findHero(any())).doReturn(hero)
        }
        val strategy = AggressiveMobStrategy(mob, mock())
        val event = strategy.takeSingleAction() as MoveEvent?
        assertNotNull(event)
        assertEquals(mob, event.obj)
        assertEquals((heroPosition - mobPosition).normalize(), event.offset)
        assertFalse(event.moveWorld)
        assertFalse(event.crazyMovements)
    }

    @Test
    fun `Test aggressive mob strategy fails`() {
        val heroPosition = Position.create(1, 1)
        val mobPosition = Position.create(0, 0)

        val hero = mock<Hero> {
            on(it.position).doReturn(heroPosition)
        }
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.findHero(any())).doReturn(hero)
        }
        val strategy = AggressiveMobStrategy(mob, mock())
        val event = strategy.takeSingleAction()
        assertNull(event)
    }

    @Test
    fun `Test attack mob strategy success`() {
        val heroPosition = Position.create(1, 1)
        val mobPosition = Position.create(0, 0)

        val hero = mock<Hero> {
            on(it.position).doReturn(heroPosition)
        }
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.findHero(any())).doReturn(hero)
            on(it.damage).doReturn(239)
        }
        val strategy = AttackMobStrategy(mob, mock())
        val event = strategy.takeSingleAction() as HPChangeEvent?
        assertNotNull(event)
        assertEquals(hero, event.entity)
        assertEquals(mob.damage, -event.amount)
    }

    @Test
    fun `Test attack mob strategy fails`() {
        val heroPosition = Position.create(1, 2)
        val mobPosition = Position.create(0, 0)

        val hero = mock<Hero> {
            on(it.position).doReturn(heroPosition)
        }
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.findHero(any())).doReturn(hero)
        }
        val strategy = AttackMobStrategy(mob, mock())
        val event = strategy.takeSingleAction()
        assertNull(event)
    }

    @Test
    fun `Test can be confused mob strategy`() {
        val mob = mock<Mob> {
            on(it.isConfused)
                .thenReturn(AtomicInteger(0))
                .thenReturn(AtomicInteger(1))
                .thenReturn(AtomicInteger(0))
        }
        val initStrategy = mock<MobStrategy> {
            on(it.takeAction()).doReturn(emptyList())
        }
        val strategy = CanBeConfusedMobStrategy(mob, mock(), initStrategy)

        assertTrue(strategy.takeAction().isEmpty())

        val event = strategy.takeAction()[0] as? MoveEvent
        assertNotNull(event)
        assertEquals(mob, event.obj)
        assertTrue(abs(event.offset.x) <= 1)
        assertTrue(abs(event.offset.y) <= 1)
        assertFalse(event.moveWorld)
        assertFalse(event.crazyMovements)

        assertTrue(strategy.takeAction().isEmpty())
    }

    @Test
    fun `Test composite mob strategy`() {
        val eventA = LetterPressedEvent('a')
        val eventB = LetterPressedEvent('b')
        val eventC = LetterPressedEvent('c')

        val strategy1 = mock<MobStrategy> {
            on(it.takeAction()).doReturn(listOf(eventA))
        }
        val strategy2 = mock<MobStrategy> {
            on(it.takeAction()).doReturn(emptyList())
        }
        val strategy3 = mock<MobStrategy> {
            on(it.takeAction()).doReturn(listOf(eventB, eventC))
        }

        val compositeStrategy = CompositeMobStrategy(mock(), mock(), listOf(strategy1, strategy2, strategy3))
        assertEquals(listOf(eventA, eventB, eventC), compositeStrategy.takeAction())
    }

    @Test
    fun `Test coward mob strategy`() {
        val heroPosition = Position.create(3, 4)
        val mobPosition = Position.create(0, 0)

        val hero = mock<Hero> {
            on(it.position).doReturn(heroPosition)
        }
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.findHero(any())).doReturn(hero)
        }
        val strategy = CowardMobStrategy(mob, mock())
        val event = strategy.takeSingleAction() as MoveEvent?
        assertNotNull(event)
        assertEquals(mob, event.obj)
        assertEquals((mobPosition - heroPosition).normalize(), event.offset)
        assertFalse(event.moveWorld)
        assertFalse(event.crazyMovements)
    }

    @Test
    fun `Test passive mob strategy`() {
        val strategy = PassiveMobStrategy(mock(), mock())
        assertTrue(strategy.takeAction().isEmpty())
    }
}
