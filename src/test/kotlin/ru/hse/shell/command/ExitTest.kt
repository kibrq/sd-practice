package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment

internal class ExitTest {
    @Test
    fun `Exit command test`() {
        val io = TestUtils.mockIO()
        val command = ExitCommand()
        val env = Environment()
        val result = command.perform(listOf(), io, env)
        assertEquals(true, result.doExit)
        assertEquals(0, result.code)
    }
}
