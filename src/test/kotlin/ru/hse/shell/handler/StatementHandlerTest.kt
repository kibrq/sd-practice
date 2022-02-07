package ru.hse.shell.handler

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.model.Statement
import ru.hse.shell.util.Environment

internal class StatementHandlerTest {
    private val handler = StatementHandler()

    private val rawCommandTestData = listOf(
        listOf("cat", "src/test/resources/cat.txt") to Pair("qwerty", 0),
        listOf("echo", "a", "b") to Pair("a b" + System.lineSeparator(), 0),
        listOf("wc", "src/test/resources/wc.txt") to Pair("2 4 17 src/test/resources/wc.txt\n", 0),
        listOf("pwd") to Pair(System.getProperty("user.dir") + System.lineSeparator(), 0),
        listOf("exit") to Pair("", 0)
    )

    @TestFactory
    fun `StatementHandler handles basic RawCommands correctly`() = rawCommandTestData.map { (args, expected) ->
        DynamicTest.dynamicTest("Command $args should write $expected") {
            val io = TestUtils.mockIO()
            val env = Environment()
            val command = Statement.RawCommand(args)
            val result = handler.handle(command, env, io)
            Assertions.assertEquals(expected.second, result.code)
            Assertions.assertEquals(expected.first, io.outputStream.toString())
        }
    }

    @Test
    fun `StatementHandler handles external command correctly`() {
        val io = TestUtils.mockIO()
        val env = Environment()
        val command = Statement.RawCommand(listOf("echo", "2", "abc"))
        val result = handler.handle(command, env, io)
        Assertions.assertEquals(0, result.code)
        Assertions.assertEquals("2 abc" + System.lineSeparator(), io.outputStream.toString())
    }

    @Test
    fun `StatementHandler handles assignment correctly`() {
        val io = TestUtils.mockIO()
        val env = Environment()
        val command = Statement.Assignment("a", "22")
        val result = handler.handle(command, env, io)
        Assertions.assertEquals(0, result.code)
        Assertions.assertEquals("", io.outputStream.toString())
        Assertions.assertEquals(env.get("a"), "22")
    }
}
