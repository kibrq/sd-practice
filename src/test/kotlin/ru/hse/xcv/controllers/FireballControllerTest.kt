package ru.hse.xcv.controllers

import org.hexworks.zircon.api.data.Position
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.view.PanelControllers
import ru.hse.xcv.world.World
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FireballControllerTest {
    @Test
    fun `Test fireball movements`() {
        var fireballDeleted = false
        var mobHP = 100

        val position = Position.create(10, 10)
        val direction = Position.create(10, 10)
        val mobPosition = Position.create(10, 7)

        val fireball = FireballSpell().createFireball(5, position, direction)
        val mob = mock<Mob> {
            on(it.position).doReturn(mobPosition)
            on(it.changeHP(any())).doAnswer { call ->
                mobHP += call.arguments[0] as Int
            }
        }
        val world = mock<World> {
            on(it.nearestVisibleObjectInRectangle(any(), any(), eq(Mob::class))).doReturn(mob)
            on(it.isEmpty(Position.create(10, 9))).doReturn(true)
            on(it.isEmpty(Position.create(10, 8))).doReturn(true)
            on(it.isEmpty(Position.create(10, 7))).doReturn(false)
            on(it.getDynamicLayer(Position.create(10, 7))).doReturn(mob)
            on(it.moveObject(any(), any())).doAnswer { call ->
                val (obj, newPosition) = call.arguments
                (obj as OnMapObject).position = newPosition as Position
                true
            }
            on(it.deleteObject(any())).doAnswer {
                fireballDeleted = true
            }
        }
        val eventBus = EventBus()
        eventBus.registerGameHandlers(world, PanelControllers(mock(), mock(), mock()))
        val controller = FireballController(fireball, world, eventBus)

        controller.action()
        assertEquals(Position.create(10, 9), fireball.position)
        assertFalse(fireballDeleted)

        controller.action()
        assertEquals(Position.create(10, 8), fireball.position)
        assertFalse(fireballDeleted)

        controller.action()
        assertTrue(fireballDeleted)
        assertEquals(100 - fireball.damage, mobHP)
    }
}
