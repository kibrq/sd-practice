package ru.hse.shell.command

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

internal class CatTest {

    private val argsTestData = listOf(
        listOf("") to Pair("", 1),
        listOf("src/test/resources/cat.txt") to Pair("qwerty", 0),
        listOf("src/test/resources/cat2.txt") to Pair("123\n456", 0),
        listOf("src/test/resources/cat.txt", "src/test/resources/cat2.txt") to Pair("qwerty\n123\n456", 0)
    )

    @TestFactory
    fun `cat command with args test`() = argsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("cat $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream("".toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = CatCommand()
            val result = command.perform(input, io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }

    private val noArgsTestData = listOf(
        "" to Pair("", 0),
        "1234" to Pair("1234", 0),
        "12\n3 4\n" to Pair("12\n3 4\n", 0)
    )

    @TestFactory
    fun `cat command without args test`() = noArgsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("cat $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream(input.toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = CatCommand()
            val result = command.perform(listOf(), io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
