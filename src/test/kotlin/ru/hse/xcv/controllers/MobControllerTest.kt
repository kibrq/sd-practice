package ru.hse.xcv.controllers

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import ru.hse.xcv.controllers.states.MobState
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class MobControllerTest {
    @Test
    fun `Test mob controller`() {
        var calledWith: MobController? = null
        val state = mock<MobState> {
            on(it.handleMobHealth(any())).doAnswer { call ->
                calledWith = call.arguments[0] as? MobController
            }
        }
        val controller = MobController(state, mock())
        controller.action()
        assertNotNull(calledWith)
        assertEquals(controller, calledWith)
    }
}
