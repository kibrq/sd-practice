package ru.hse.shell.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils.Companion.mockIO

internal class GrepTest {
    @Test
    fun `test test`() {
        val io = mockIO()
        val command = GrepCommand()
        val result = command.perform(listOf("qwe", "src/test/resources/cat.txt"), io)
        Assertions.assertEquals(0, result.code)
    }
}
