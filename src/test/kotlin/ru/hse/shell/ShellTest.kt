package ru.hse.shell

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.handler.ExpressionHandler
import ru.hse.shell.parser.Parser
import ru.hse.shell.util.Environment

internal class ShellTest {
    private val handler = ExpressionHandler()

    private val expressionTestData = listOf(
        listOf("echo 123") to Pair(listOf("123" + System.lineSeparator()), listOf(0)),
        listOf("a=\"ec\"", "b=\"ho\"", "\$a\$b 123") to Pair(
            listOf("", "", "123" + System.lineSeparator()),
            listOf(0, 0, 0)
        ),
        listOf("a=\"ec\"", "echo \$a", "exit") to Pair(
            listOf(
                "",
                "ec" + System.lineSeparator(),
                "ec" + System.lineSeparator()
            ), listOf(0, 0, 0)
        ),
        listOf("pwd | echo 123") to Pair(listOf("123" + System.lineSeparator()), listOf(0)),
        listOf("exit | echo 123") to Pair(listOf(""), listOf(0))
    )

    @TestFactory
    fun `ExpressionHandler handles list of RawCommands correctly`() =
        expressionTestData.map { (commands, expected) ->
            DynamicTest.dynamicTest("Command list $commands should write $expected") {
                val io = TestUtils.mockIO()
                val env = Environment()
                for (i in commands.indices) {
                    val statements = Parser.parseToEnd(commands[i])
                    val exitCode = handler.handle(statements, env, io)
                    Assertions.assertEquals(expected.second[i], exitCode.code)
                    Assertions.assertEquals(expected.first[i], io.outputStream.toString())
                }
            }
        }
}
