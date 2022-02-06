package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class WcTest {
    private val argsTestData = listOf(
        listOf("") to Pair("", 1),
        listOf("src/test/resources/wc.txt") to Pair("2 4 17 src/test/resources/wc.txt\n", 0),
        listOf("src/test/resources/wc2.txt") to Pair("5 5 9 src/test/resources/wc2.txt\n", 0),
        listOf("src/test/resources/wc.txt", "src/test/resources/wc2.txt") to Pair(
            "2 4 17 src/test/resources/wc.txt\n" + "5 5 9 src/test/resources/wc2.txt\n" + "7 9 26 total\n", 0
        )
    )

    @TestFactory
    fun `Wc command with args test`() = argsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("wc $input should return $expected") {
            val io = TestUtils.mockIO()
            val command = WcCommand()
            val result = command.perform(input, io)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }


    private val noArgsTestData = listOf(
        "" to Pair("0 0 0 total\n", 0),
        "1234 5" to Pair("1 2 6 total\n", 0),
        "12\n3\t4\n" to Pair("2 3 7 total\n", 0)
    )

    @TestFactory
    fun `Wc command without args test`() = noArgsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("wc $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream(input.toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = WcCommand()
            val result = command.perform(listOf(), io)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
