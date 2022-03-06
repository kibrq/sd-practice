package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment

internal class EchoTest {
    private val commandTestData = listOf(
        listOf("a") to Pair("a" + System.lineSeparator(), 0),
        listOf("a", "b") to Pair("a b" + System.lineSeparator(), 0),
        listOf("a  b", "c  ", "d") to Pair("a  b c   d" + System.lineSeparator(), 0)
    )

    @TestFactory
    fun `Echo command test`() = commandTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("echo $input should return $expected") {
            val io = TestUtils.mockIO()
            val command = EchoCommand()
            val env = Environment()
            val result = command.perform(input, io, env)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
