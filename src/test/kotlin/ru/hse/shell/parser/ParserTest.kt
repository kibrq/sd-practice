package ru.hse.shell.parser

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.model.Statement

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

    @TestFactory
    fun `Parsing of such input should fail`() = (failCommandTestData + failAssignmentData).map { input ->
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
            val statement = Parser.parseToEnd(input)
            if (statement is Statement.RawCommand) {
                assertEquals(expected, statement.arguments)
            } else {
                assert(false) { "Parsed as not a command" }
            }
        }
    }

    private val assignmentTestData = listOf(
        "a=b" to listOf("a", "b"),
        "a=bb" to listOf("a", "bb"),
        "aa=b" to listOf("aa", "b"),
        "a=b' \"a'" to listOf("a", "b \"a")
    )

    @TestFactory
    fun `LHS=RHS should parsed as an assignment`() = assignmentTestData.map { (input, expected) ->
        DynamicTest.dynamicTest("$input should be parsed as ${expected[0]} assign ${expected[1]}") {
            val statement = Parser.parseToEnd(input)
            if (statement is Statement.Assignment) {
                assertEquals(expected[0], statement.name)
                assertEquals(expected[1], statement.value)
            } else {
                assert(false) { "Parsed as not an assignment" }
            }
        }
    }
}
