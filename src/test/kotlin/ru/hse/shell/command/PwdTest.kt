package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment

internal class PwdTest {
    @Test
    fun `Pwd command test`() {
        val io = TestUtils.mockIO()
        val command = PwdCommand()
        val env = Environment()
        val result = command.perform(listOf(), io, env)
        assertEquals(0, result.code)
        assertEquals(System.getProperty("user.dir") + System.lineSeparator(), io.outputStream.toString())
    }
}
