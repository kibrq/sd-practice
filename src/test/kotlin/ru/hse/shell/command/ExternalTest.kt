package ru.hse.shell.command

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.util.Environment
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class ExternalTest {

    private val testData = listOf(
        Pair("pwd", emptyList<String>()) to Pair(System.getProperty("user.dir") + System.lineSeparator(), 0),
        Pair("echo", listOf("3", "3")) to Pair("3 3\n", 0),
        Pair("sleep", listOf("3")) to Pair("", 0),
        Pair("maslo", emptyList<String>()) to Pair("", 1)
    )

    @TestFactory
    fun `external command test`() = testData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream("".toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val env = Environment()
            val command = ExternalCommand(input.first, env)
            val result = command.perform(input.second, io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }

}
