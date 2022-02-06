package ru.hse.shell.parser

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import ru.hse.shell.model.Statement

/*
 * The object describing the grammar parser.
 * Usage: `val statement = Parser.parseToEnd(string)`.
 */
object Parser : Grammar<Statement>() {
    private val singleQuoteToken by literalToken("'")
    private val doubleQuoteToken by literalToken("\"")
    private val assignmentToken by literalToken("=")
    private val whitespaceToken by regexToken("\\s+")
    private val symbolsToken by regexToken("[^'\"=\\s+]+")

    private val singleQuote by singleQuoteToken map { it.text }
    private val doubleQuote by doubleQuoteToken map { it.text }
    private val assignment by assignmentToken map { it.text }
    private val whitespace by whitespaceToken map { it.text }
    private val symbols by symbolsToken map { it.text }

    private val insideSingleQuotes by zeroOrMore(
        OrCombinator(listOf(doubleQuote, whitespace, symbols, assignment))
    ) map { it.reduceOrNull { acc, string -> acc + string } ?: "" }

    private val insideDoubleQuotes by zeroOrMore(
        OrCombinator(listOf(singleQuote, whitespace, symbols, assignment))
    ) map { it.reduceOrNull { acc, string -> acc + string } ?: "" }

    private val singleQuotes by skip(singleQuote) and insideSingleQuotes and skip(singleQuote)
    private val doubleQuotes by skip(doubleQuote) and insideDoubleQuotes and skip(doubleQuote)
    private val evalString by oneOrMore(singleQuotes or doubleQuotes or symbols) map {
        it.reduceOrNull { acc, string -> acc + string } ?: ""
    }
    private val assignmentExpr by symbols and skip(assignmentToken) and evalString map { (name, value) ->
        Statement.Assignment(name, value)
    }
    private val rawCommand by separated(
        evalString,
        whitespaceToken,
        acceptZero = false
    ) map { Statement.RawCommand(it.terms) }
    private val statement by assignmentExpr or rawCommand

    override val rootParser: Parser<Statement> by statement
}
