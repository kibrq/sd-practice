package ru.hse.shell.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils.Companion.mockIO
import ru.hse.shell.TestUtils.Companion.newline

internal class GrepTest {
    private val grepTestFile = "src/test/resources/grep.txt"

    private val commandTestData = listOf(
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
    fun `Grep command test`() = commandTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("echo $input should return $expected") {
            val io = mockIO()
            val command = GrepCommand()
            val result = command.perform(input + grepTestFile, io)
            Assertions.assertEquals(expected.second, result.code)
            Assertions.assertEquals(expected.first, io.outputStream.toString())
        }
    }
}
