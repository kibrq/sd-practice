package ru.hse.shell.command

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

internal class EchoTest {

    private val commandTestData = listOf(
        listOf("a") to Pair("a", 0),
        listOf("a", "b") to Pair("a b", 0),
        listOf("a  b", "c  ", "d") to Pair("a  b c   d", 0)
    )

    @TestFactory
    fun `echo command test`() = commandTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("echo $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream("".toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = EchoCommand()
            val result = command.perform(input, io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
