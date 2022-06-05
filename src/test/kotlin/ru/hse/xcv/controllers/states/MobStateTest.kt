package ru.hse.xcv.controllers.states

import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.hse.xcv.controllers.MobController
import ru.hse.xcv.controllers.strategies.PassiveMobStrategy
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.world.World
import kotlin.test.assertTrue

internal class MobStateTest {
    @Test
    fun `Test mob state changes`() {
        val world = mock<World>()
        val mob = mock<Mob> {
            on(it.panicHealthThreshold).doReturn(10)
            on(it.currentHealth)
                .thenReturn(100)
                .thenReturn(50)
                .thenReturn(11)
                .thenReturn(10)
                .thenReturn(11)
                .thenReturn(0)
        }
        val strategy = PassiveMobStrategy(mob, world)
        val state = NormalMobState(strategy)
        val controller = MobController(state, mock())

        controller.state.handleMobHealth(controller)
        assertTrue(NormalMobState::class.isInstance(controller.state))

        controller.state.handleMobHealth(controller)
        assertTrue(NormalMobState::class.isInstance(controller.state))

        controller.state.handleMobHealth(controller)
        assertTrue(NormalMobState::class.isInstance(controller.state))

        controller.state.handleMobHealth(controller)
        assertTrue(PanicMobState::class.isInstance(controller.state))

        controller.state.handleMobHealth(controller)
        assertTrue(NormalMobState::class.isInstance(controller.state))

        controller.state.handleMobHealth(controller)
        assertTrue(PanicMobState::class.isInstance(controller.state))
    }
}
