package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils

internal class EchoTest {
    private val commandTestData = listOf(
        listOf("a") to Pair("a\n", 0),
        listOf("a", "b") to Pair("a b\n", 0),
        listOf("a  b", "c  ", "d") to Pair("a  b c   d\n", 0)
    )

    @TestFactory
    fun `Echo command test`() = commandTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("echo $input should return $expected") {
            val io = TestUtils.mockIO()
            val command = EchoCommand()
            val result = command.perform(input, io)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
