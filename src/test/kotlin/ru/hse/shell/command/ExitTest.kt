package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils

internal class ExitTest {
    @Test
    fun `Exit command test`() {
        val io = TestUtils.mockIO()
        val command = ExitCommand()
        val result = command.perform(listOf(), io)
        assertEquals(true, result.isExit)
        assertEquals(0, result.code)
    }
}
