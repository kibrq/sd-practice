package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment

internal class ExternalTest {
    private val linuxTestData = listOf(
        Pair("pwd", emptyList<String>()) to Pair(System.getProperty("user.dir") + System.lineSeparator(), 0),
        Pair("echo", listOf("3", "3")) to Pair("3 3" + System.lineSeparator(), 0),
        Pair("sleep", listOf("3")) to Pair("", 0),
        Pair("maslo", emptyList<String>()) to Pair("", 1)
    )

    private val windowsTestData = listOf(
        Pair("echo", listOf("%cd%")) to Pair(System.getProperty("user.dir") + System.lineSeparator(), 0),
        Pair("echo", listOf("3", "3")) to Pair("3 3" + System.lineSeparator(), 0),
        Pair("maslo", emptyList<String>()) to Pair("", 1)
    )

    @TestFactory
    fun `External command test`() = (if (System.getProperty("os.name").startsWith("Win")) windowsTestData else linuxTestData).map { (input, expected) ->
        DynamicTest.dynamicTest("$input should return $expected") {
            val io = TestUtils.mockIO()
            val env = Environment()
            val command = ExternalCommand(input.first)
            val result = command.perform(input.second, io, env)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
