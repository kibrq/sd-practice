package ru.hse.shell.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.ByteArrayOutputStream

internal class StreamUtilsTest {
    private val testData = listOf(
        Pair("", false) to "",
        Pair("", true) to System.lineSeparator(),
        Pair("qwe\n\trty", false) to "qwe\n\trty",
        Pair("qwe\n\trty", true) to "qwe\n\trty" + System.lineSeparator(),
        Pair("qwerty\n\n", false) to "qwerty\n\n",
        Pair("qwerty\n\n", true) to "qwerty\n\n" + System.lineSeparator(),
        Pair("\n", false) to "\n",
        Pair("\n", true) to "\n" + System.lineSeparator()
    )

    @TestFactory
    fun `Write to stream works correctly`() = testData.map { (input, expected) ->
        DynamicTest.dynamicTest("writeToStream($input) should write $expected") {
            val out = ByteArrayOutputStream()
            StreamUtils.writeToStream(out, input.first, input.second)
            assertEquals(expected, out.toString())
        }
    }
}
