package ru.hse.shell.handler

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.model.EvalString.Companion.ofList
import ru.hse.shell.model.EvalString.Companion.ofVariable
import ru.hse.shell.model.Statement
import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode

internal class ExpressionHandlerTest {
    private val handler = ExpressionHandler()

    private val expressionTestData = listOf(
        listOf(listOf("cat", "src/test/resources/cat.txt"), listOf("wc")) to Pair("1 1 6" + System.lineSeparator(), 0),
        listOf(
            listOf("echo", "123"),
            listOf("wc")
        ) to Pair("1 1 ${3 + System.lineSeparator().length}" + System.lineSeparator(), 0),
        listOf(listOf("echo", "123"), listOf("echo")) to Pair(System.lineSeparator(), 0),
        listOf(listOf("echo", "123"), listOf("exit"), listOf("echo")) to Pair("", 0)
    )

    @TestFactory
    fun `ExpressionHandler handles list of RawCommands correctly`() =
        expressionTestData.map { (commandsWithArgs, expected) ->
            DynamicTest.dynamicTest("Command list $commandsWithArgs should write $expected") {
                val io = TestUtils.mockIO()
                val env = Environment()
                val commands = commandsWithArgs.map { args -> Statement.RawCommand(TestUtils.evalStrings(args)) }
                val result = handler.handle(commands, env, io)
                Assertions.assertEquals(expected.second, result.code)
                Assertions.assertEquals(expected.first, io.outputStream.toString())
            }
        }


    private val expressionTestDataWithEnv = listOf(
        Pair(
            listOf(
                listOf(ofList(listOf(ofVariable("a"), ofVariable("b"))), TestUtils.evalString("123")),
                listOf(TestUtils.evalString("wc"))
            ),
            mapOf("a" to "ec", "b" to "ho", "c" to "123", "echo" to "qwerty")
        ) to Pair("1 1 ${3 + System.lineSeparator().length}" + System.lineSeparator(), ExitCode.success()),
        Pair(
            listOf(listOf(ofList(listOf(ofVariable("a"), ofVariable("b"))))),
            mapOf("a" to "ex", "b" to "it")
        ) to Pair("", ExitCode.exit())
    )

    @TestFactory
    fun `ExpressionHandler handles list of RawCommands with custom env correctly`() =
        expressionTestDataWithEnv.map { (commandsWithArgs, expected) ->
            DynamicTest.dynamicTest("Command list ${commandsWithArgs.first} with env ${commandsWithArgs.second} should write $expected") {
                val io = TestUtils.mockIO()
                val env = Environment()
                for (entry in commandsWithArgs.second.entries) {
                    env.put(entry.key, entry.value)
                }
                val commands = commandsWithArgs.first.map { args -> Statement.RawCommand(args) }
                val result = handler.handle(commands, env, io)
                Assertions.assertEquals(expected.second, result)
                Assertions.assertEquals(expected.first, io.outputStream.toString())
            }
        }
}
