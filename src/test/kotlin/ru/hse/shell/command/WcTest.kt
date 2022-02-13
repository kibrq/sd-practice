package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

internal class WcTest {
    private val argsTestData = listOf(
        listOf("") to Pair("", 1),
        listOf("src/test/resources/wc.txt") to Pair(
            "2 4 ${File("src/test/resources/wc.txt").length()} src/test/resources/wc.txt" + System.lineSeparator(),
            0
        ),
        listOf("src/test/resources/wc2.txt") to Pair(
            "5 5 ${File("src/test/resources/wc2.txt").length()} src/test/resources/wc2.txt" + System.lineSeparator(),
            0
        ),
        listOf("src/test/resources/wc.txt", "src/test/resources/wc2.txt") to Pair(
            "2 4 ${File("src/test/resources/wc.txt").length()} src/test/resources/wc.txt" + System.lineSeparator()
                    + "5 5 ${File("src/test/resources/wc2.txt").length()} src/test/resources/wc2.txt" + System.lineSeparator()
                    + "7 9 ${File("src/test/resources/wc.txt").length() + File("src/test/resources/wc2.txt").length()} total" + System.lineSeparator(),
            0
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
        "" to Pair("0 0 0" + System.lineSeparator(), 0),
        "1234 5" to Pair("1 2 6" + System.lineSeparator(), 0),
        "12\n3\t4\n" to Pair("2 3 7" + System.lineSeparator(), 0)
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
