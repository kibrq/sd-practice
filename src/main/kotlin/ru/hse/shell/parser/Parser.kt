package ru.hse.shell.parser

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import ru.hse.shell.model.Statement


object Parser : Grammar<Statement>() {
    val singleQuoteToken by literalToken("'")
    val doubleQuoteToken by literalToken("\"")
    val assignmentToken by literalToken("=")
    val whitespaceToken by regexToken("\\s+")
    val symbolsToken by regexToken("[^'\"=\\s+]+")

    val singleQuote by singleQuoteToken map { it.text }
    val doubleQuote by doubleQuoteToken map { it.text }
    val assignment by assignmentToken map { it.text }
    val whitespace by whitespaceToken map { it.text }
    val symbols by symbolsToken map { it.text }

    val insideSingleQuotes by zeroOrMore(
        OrCombinator(listOf(doubleQuote, whitespace, symbols, assignment))
    ) map { it.reduceOrNull { acc, string -> acc + string } ?: "" }

    val insideDoubleQuotes by zeroOrMore(
        OrCombinator(listOf(singleQuote, whitespace, symbols, assignment))
    ) map { it.reduceOrNull { acc, string -> acc + string } ?: "" }

    val singleQuotes by skip(singleQuote) and insideSingleQuotes and skip(singleQuote)
    val doubleQuotes by skip(doubleQuote) and insideDoubleQuotes and skip(doubleQuote)
    val evalString by oneOrMore(singleQuotes or doubleQuotes or symbols) map {
        it.reduceOrNull { acc, string -> acc + string } ?: ""
    }
    val assignmentExpr by symbols and skip(assignmentToken) and evalString map { (name, value) ->
        Statement.Assignment(name, value)
    }
    val rawCommand by separated(
        evalString,
        whitespaceToken,
        acceptZero = false
    ) map { Statement.RawCommand(it.terms) }
    val statement by this.assignmentExpr or this.rawCommand

    override val rootParser: Parser<Statement> by statement
}
