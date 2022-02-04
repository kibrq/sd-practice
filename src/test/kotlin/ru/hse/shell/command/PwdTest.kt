package ru.hse.shell.command

import org.junit.jupiter.api.Test
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

internal class PwdTest {

    @Test
    fun `pwd command test`() {
        val io = IO(
            inputStream = ByteArrayInputStream("".toByteArray()),
            outputStream = ByteArrayOutputStream(),
            errorStream = ByteArrayOutputStream()
        )
        val command = PwdCommand()
        val result = command.perform(listOf(), io)
        assertEquals(0, result.code)
        assertEquals(System.getProperty("user.dir"), io.outputStream.toString())
    }
}
