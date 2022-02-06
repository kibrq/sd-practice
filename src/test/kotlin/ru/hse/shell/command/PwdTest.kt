package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils

internal class PwdTest {
    @Test
    fun `Pwd command test`() {
        val io = TestUtils.mockIO()
        val command = PwdCommand()
        val result = command.perform(listOf(), io)
        assertEquals(0, result.code)
        assertEquals(System.getProperty("user.dir") + "\n", io.outputStream.toString())
    }
}
