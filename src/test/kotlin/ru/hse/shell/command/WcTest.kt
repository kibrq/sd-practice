package ru.hse.shell.command

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class WcTest {
    private val argsTestData = listOf(
        listOf("") to Pair("", 1),
        listOf("src/test/resources/wc.txt") to Pair("2 4 17 src/test/resources/wc.txt", 0),
        listOf("src/test/resources/wc2.txt") to Pair("5 5 9 src/test/resources/wc2.txt", 0),
        listOf("src/test/resources/wc.txt", "src/test/resources/wc2.txt") to Pair(
            "2 4 17 src/test/resources/wc.txt\n" + "5 5 9 src/test/resources/wc2.txt\n" + "7 9 26 total", 0
        ),
    )

    @TestFactory
    fun wcTestWithArgs() = argsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("wc $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream("".toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = WcCommand()
            val result = command.perform(input, io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }


    private val noArgsTestData = listOf(
        "" to Pair("1 0 0 total", 0),
        "1234 5" to Pair("1 2 6 total", 0),
        "12\n3 4\n" to Pair("3 3 7 total", 0)
    )


    @TestFactory
    fun wcTestWithNoArgs() = noArgsTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("wc $input should return $expected") {
            val io = IO(
                inputStream = ByteArrayInputStream(input.toByteArray()),
                outputStream = ByteArrayOutputStream(),
                errorStream = ByteArrayOutputStream()
            )
            val command = WcCommand()
            val result = command.perform(listOf(), io)
            assertEquals(expected.second, result.exitCode)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
