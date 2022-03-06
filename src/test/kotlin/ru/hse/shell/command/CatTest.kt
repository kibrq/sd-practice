package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class CatTest {
    private val argsTestData = listOf(
        listOf("") to Pair("", 1),
        listOf("src/test/resources/cat.txt") to Pair("qwerty", 0),
        listOf("src/test/resources/cat2.txt") to Pair("123${System.lineSeparator()}456", 0),
        listOf("src/test/resources/cat.txt", "src/test/resources/cat2.txt") to Pair("qwerty123${System.lineSeparator()}456", 0)
    )

    @TestFactory
    fun `Cat command with args test`() = argsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("cat $input should return $expected") {
            val io = TestUtils.mockIO()
            val command = CatCommand()
            val env = Environment()
            val result = command.perform(input, io, env)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }

    private val noArgsTestData = listOf(
        "" to Pair("", 0),
        "1234" to Pair("1234", 0),
        "12\n3 4\n" to Pair("12\n3 4\n", 0)
    )

    @TestFactory
    fun `Cat command without args test`() = noArgsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("cat $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream(input.toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = CatCommand()
            val env = Environment()
            val result = command.perform(listOf(), io, env)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
