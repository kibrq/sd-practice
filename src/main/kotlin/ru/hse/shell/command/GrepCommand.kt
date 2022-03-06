package ru.hse.shell.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.util.stream.Stream

/*
 * Bash's 'grep' analogue: finds pattern as substring in given input.
 */
class GrepCommand : Command {
    /*
     * Execute the 'grep' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        val grepArgs = GrepArgs(args)
        val matcher = matcher(grepArgs.findAsWord, grepArgs.pattern())

        val lines = when {
            grepArgs.sources.isEmpty() -> io.inputStream.bufferedReader().lines()
            else -> {
                var stream = Stream.empty<String>()
                for (filename in grepArgs.sources) {
                    stream =
                        Stream.concat(stream, env.resolveCurrentDirectory(filename).toFile().bufferedReader().lines())
                }
                stream
            }
        }

        var context = 0
        lines.forEachOrdered {
            // If contexts intersect, then `context` just resets
            if (matcher.matches(it)) {
                context = grepArgs.numberOfLines + 1
            }
            if (context > 0) {
                StreamUtils.writeToStream(io.outputStream, it, true)
            }
            context--
        }

        return ExitCode.success()
    }

    private sealed class Matcher(val pattern: Regex) {
        abstract fun matches(target: String): Boolean

        /*
         * Grep matcher with '-w' option: the pattern is searched for as a word.
         */
        class WordMatcher(pattern: Regex) : Matcher(pattern) {
            override fun matches(target: String): Boolean = target.split(Regex("\\s+")).any { pattern matches it }
        }

        /*
         * Default pattern matcher.
         */
        class LineMatcher(pattern: Regex) : Matcher(pattern) {
            override fun matches(target: String) = pattern.containsMatchIn(target)
        }
    }

    private fun matcher(findAsWord: Boolean, pattern: Regex) =
        if (findAsWord) Matcher.WordMatcher(pattern) else Matcher.LineMatcher(pattern)


    private class GrepArgs(rawArgs: List<String>) : CliktCommand() {
        private val pattern: String by argument(help = "Pattern to find")
        private val caseInsensitive: Boolean by option("-i", help = "Case-insensitive search").flag()
        val findAsWord: Boolean by option("-w", help = "Find as word").flag()
        val numberOfLines: Int by option("-A", help = "Number of adjacent rows in the output").int()
            .restrictTo(0..Int.MAX_VALUE, clamp = true).default(0)
        val sources: List<String> by argument(help = "List of sources").multiple()
        override fun run() = Unit

        init {
            parse(rawArgs)
        }

        /*
         * Generate `Regex` object from the stored arguments.
         */
        fun pattern(): Regex {
            val options = mutableSetOf<RegexOption>()
            if (caseInsensitive) {
                options.add(RegexOption.IGNORE_CASE)
            }
            return Regex(pattern = pattern, options = options)
        }
    }
}
