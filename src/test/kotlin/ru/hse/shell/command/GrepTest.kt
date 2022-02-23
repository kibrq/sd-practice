package ru.hse.shell.command

import com.github.ajalt.clikt.core.BadParameterValue
import com.github.ajalt.clikt.core.MissingArgument
import com.github.ajalt.clikt.core.NoSuchOption
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils.Companion.mockIO
import ru.hse.shell.TestUtils.Companion.newline

internal class GrepTest {
    private val grepTestFile = "src/test/resources/grep.txt"

    private val correctTestData = listOf(
        listOf("pena14") to Pair(newline("pena14"), 0),
        listOf("pe") to Pair(newline("pena14") + newline("pe na") + newline("pena portal \t5"), 0),
        listOf("pena") to Pair(newline("pena14") + newline("pena portal \t5"), 0),
        listOf("pena", "-i") to Pair(newline("pena14") + newline("pena portal \t5") + newline("PeNa"), 0),
        listOf("pena", "-w") to Pair(newline("pena portal \t5"), 0),
        listOf("pena", "-A", "0") to Pair(newline("pena14") + newline("pena portal \t5"), 0),
        listOf("pena", "-A", "-1") to Pair(newline("pena14") + newline("pena portal \t5"), 0),
        listOf("pena", "-A", "1") to Pair(
            newline("pena14") + newline("pe na") + newline("pena portal \t5") + newline("9393"), 0
        ),
        listOf("pena", "-A", "2") to Pair(
            newline("pena14") + newline("pe na") + newline("213") +
                    newline("pena portal \t5") + newline("9393") + newline("PeNa"), 0
        )
    )

    @TestFactory
    fun `Grep command test`() = correctTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("grep $input should return $expected") {
            val io = mockIO()
            val command = GrepCommand()
            val result = command.perform(input + grepTestFile, io)
            Assertions.assertEquals(expected.second, result.code)
            Assertions.assertEquals(expected.first, io.outputStream.toString())
        }
    }

    private fun <T : Throwable> testGrepThrowsWithException(args: List<String>, clazz: Class<T>) =
        DynamicTest.dynamicTest("grep $args should fail with given exception") {
            val io = mockIO()
            val command = GrepCommand()
            Assertions.assertThrows(clazz) {
                command.perform(args + grepTestFile, io)
            }
        }

    private val noSuchOptionTestData = listOf(
        listOf("-x"),
        listOf("-W")
    )

    private val badParameterValueTestData = listOf(
        listOf("-A", "xxx"),
        listOf("-A", "-w")
    )

    private val missingArgumentTestData = listOf(
        listOf("-A"),
        listOf("-i", "-A"),
        listOf("-wA")
    )

    @TestFactory
    fun `Grep command no such option`() = noSuchOptionTestData.map {
        testGrepThrowsWithException(it, NoSuchOption::class.java)
    }

    @TestFactory
    fun `Grep command bad parameter value`() = badParameterValueTestData.map {
        testGrepThrowsWithException(it, BadParameterValue::class.java)
    }

    @TestFactory
    fun `Grep command missing argument`() = missingArgumentTestData.map {
        testGrepThrowsWithException(it, MissingArgument::class.java)
    }

    @Test
    fun `Grep command should fail on empty arguments`() {
        val io = mockIO()
        val command = GrepCommand()
        Assertions.assertThrows(MissingArgument::class.java) {
            command.perform(emptyList(), io)
        }
    }
}
