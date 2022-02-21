package ru.hse.shell.parser

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import ru.hse.shell.model.EvalString
import ru.hse.shell.model.Statement

/*
 * The object describing the grammar parser.
 * Usage: `val statement = Parser.parseToEnd(string)`.
 */
object Parser : Grammar<List<Statement>>() {
    private val singleQuoteToken by literalToken("'")
    private val doubleQuoteToken by literalToken("\"")
    private val assignmentToken by literalToken("=")
    private val whitespaceToken by regexToken("\\s+")
    private val dollarToken by literalToken("$")
    private val pipeToken by literalToken("|")
    private val symbolsToken by regexToken("[^|\$'\"=\\s+]+")

    private val singleQuote by singleQuoteToken map { it.text }
    private val doubleQuote by doubleQuoteToken map { it.text }
    private val assignment by assignmentToken map { it.text }
    private val dollar by dollarToken map { it.text }
    private val pipe by pipeToken map { it.text }
    private val whitespace by whitespaceToken map { it.text }
    private val symbols by symbolsToken map { it.text }

    private val variable by skip(dollar) and symbols

    private val insideSingleQuotes by zeroOrMore(
        OrCombinator(listOf(doubleQuote, whitespace, symbols, assignment, dollar, variable, pipe))
    ) map { it.reduceOrNull { acc, string -> acc + string } ?: "" }

    private val insideDoubleQuotes by zeroOrMore(
        OrCombinator(
            listOf(
                singleQuote.map { EvalString.ofString(it) },
                whitespace.map { EvalString.ofString(it) },
                symbols.map { EvalString.ofString(it) },
                assignment.map { EvalString.ofString(it) },
                variable.map { EvalString.ofVariable(it) },
                dollar.map { EvalString.ofString(it) },
                pipe.map { EvalString.ofString(it) }
            )
        )
    ) map { EvalString.ofList(it) }

    private val singleQuotes by skip(singleQuote) and insideSingleQuotes and skip(singleQuote)
    private val doubleQuotes by skip(doubleQuote) and insideDoubleQuotes and skip(doubleQuote)

    private val evalString by oneOrMore(
        OrCombinator(listOf(
            singleQuotes.map { EvalString.ofString(it) },
            doubleQuotes,
            symbols.map { EvalString.ofString(it) },
            variable.map { EvalString.ofVariable(it) },
            dollar.map { EvalString.ofString(it) }
        ))
    ) map { EvalString.ofList(it) }

    private val assignmentExpr by symbols and skip(assignment) and evalString map { (name, value) ->
        Statement.Assignment(name, value)
    }

    private val rawCommand by separated(
        evalString,
        whitespace,
        acceptZero = false
    ) map { Statement.RawCommand(it.terms) }

    private val whitespacedCommand by skip(zeroOrMore(whitespace)) and rawCommand and skip(zeroOrMore(whitespace))

    private val statement by assignmentExpr or whitespacedCommand

    override val rootParser: Parser<List<Statement>> =
        separated(statement, pipe, acceptZero = false) map { it.terms }
}
