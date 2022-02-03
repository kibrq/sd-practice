package ru.hse.shell.command

import org.junit.jupiter.api.Test
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class ExitTest {

    @Test
    fun testExit() {
        val io = IO(
            inputStream = ByteArrayInputStream("".toByteArray()),
            outputStream = ByteArrayOutputStream(),
            errorStream = ByteArrayOutputStream()
        )
        val command = ExitCommand()
        val result = command.perform(listOf(), io)
        assertEquals(true, result.isExit)
        assertEquals(0, result.exitCode)
    }
}
