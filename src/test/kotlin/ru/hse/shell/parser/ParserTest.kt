package ru.hse.shell.parser

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.model.Statement
import ru.hse.shell.util.Environment

internal class ParserTest {
    private val failCommandTestData = listOf(
        "a'",
        "a\"",
        "",
        "a'''"
    )

    private val failAssignmentData = listOf(
        "a=",
        "a=a'",
        "=a",
        "a' b'="
    )

    private val failPipeData = listOf(
        "abs |",
        "| asd",
        "|"
    )

    @TestFactory
    fun `Parsing of such input should fail`() = (failCommandTestData + failAssignmentData + failPipeData).map { input ->
        DynamicTest.dynamicTest("Parsing of $input should fail") {
            assertThrows(Exception::class.java) { Parser.parseToEnd(input) }
        }
    }

    private val commandTestData = listOf(
        "a" to listOf("a"),
        "ab" to listOf("ab"),
        "a-a" to listOf("a-a"),
        "a_a" to listOf("a_a"),
        "a'b b'" to listOf("ab b"),
        "a\"b b\"" to listOf("ab b"),
        "a b" to listOf("a", "b"),
        "a-a b_b" to listOf("a-a", "b_b"),
        "'a \"a' \"'\" b" to listOf("a \"a", "'", "b"),
        "a '' ''" to listOf("a", "", ""),
        "a'a=b'" to listOf("aa=b"),
        "\"=\"a" to listOf("=a")
    )

    @TestFactory
    fun `Sequence of words should be parsed to command`() = commandTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should parsed as one-word command named $expected") {
            val statement = Parser.parseToEnd(input).first()
            if (statement is Statement.RawCommand) {
                assertEquals(expected, statement.arguments.map { it.eval(Environment()) })
            } else {
                assert(false) { "Parsed as not a command" }
            }
        }
    }

    private val assignmentTestData = listOf(
        "a=b" to Pair("a", "b"),
        "a=bb" to Pair("a", "bb"),
        "aa=b" to Pair("aa", "b"),
        "a=b' \"a'" to Pair("a", "b \"a")
    )

    @TestFactory
    fun `LHS=RHS should parsed as an assignment`() = assignmentTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should be parsed as ${expected.first} assign ${expected.second}") {
            val statement = Parser.parseToEnd(input).first()
            if (statement is Statement.Assignment) {
                assertEquals(expected.first, statement.name)
                assertEquals(expected.second, statement.value.eval(Environment()))
            } else {
                assert(false) { "Parsed as not an assignment" }
            }
        }
    }

    private val substituteToEmptyTestData = listOf(
        "ab\$a" to listOf("ab"),
        "ab\$ rt\$rt" to listOf("ab\$", "rt"),
        "ab\$ab'a'\$cd" to listOf("aba"),
        "ab\"\$longlongvariable\"" to listOf("ab"),
        "\"ab\$\"" to listOf("ab\$")
    )

    @TestFactory
    fun `Substitute to empty string`() = substituteToEmptyTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should parsed and evaluate to $expected") {
            val statement = Parser.parseToEnd(input).first()
            if (statement is Statement.RawCommand) {
                assertEquals(expected, statement.arguments.map { it.eval(Environment()) })
            } else {
                assert(false) { "Parsed as not a command" }
            }
        }
    }

    private val pipeTestData = listOf(
        "abc | abc" to listOf(listOf("abc"), listOf("abc")),
        "ab'|'c" to listOf(listOf("ab|c")),
        "ab\"\$a|\"" to listOf(listOf("ab|"))
    )

    @TestFactory
    fun `Parsing pipe`() = pipeTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should parsed and evaluate to $expected") {
            val env = Environment()
            val statements: MutableList<List<String>> = mutableListOf()
            Parser.parseToEnd(input).forEach {
                if (it is Statement.RawCommand) {
                    statements.add(it.arguments.map { its -> its.eval(env) })
                }
            }
            assertEquals(expected, statements)
        }
    }
}
