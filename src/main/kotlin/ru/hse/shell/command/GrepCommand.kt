package ru.hse.shell.command


import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.io.File

class GrepCommand : Command {

    override fun perform(args: List<String>, io: IO): ExitCode {
        val grepArgs = GrepArgs(args)
        val matcher = matcher(grepArgs.findAsWord, grepArgs.pattern())
        for (filename in grepArgs.sources) {
            var context = 0
            File(filename).forEachLine {
                
                // If context's intersect, then `context` just reset
                if (matcher.matches(it)) {
                    context = grepArgs.numberOfLines + 1
                }
                if (context > 0) {
                    StreamUtils.writeToStream(io.outputStream, it, true)
                }
                context--
            }
        }
        return ExitCode.success()
    }

    // Class encapsulating logic of matching
    private sealed class Matcher(val pattern: Regex) {

        abstract fun matches(target: String): Boolean

        // Matcher if -w was in arguments
        class WordMatcher(pattern: Regex) : Matcher(pattern) {
            override fun matches(target: String): Boolean = target.split("\\s+").any { pattern matches it }
        }

        // Default matcher
        class LineMatcher(pattern: Regex) : Matcher(pattern) {
            override fun matches(target: String) = pattern matches target
        }
    }

    private fun matcher(findAsWord: Boolean, pattern: Regex) =
        if (findAsWord) Matcher.WordMatcher(pattern) else Matcher.LineMatcher(pattern)

    // Argument parser for `grep` command
    private class GrepArgs(rawArgs: List<String>) : CliktCommand() {
        val findAsWord: Boolean by option("-w", help = "Find as word").flag()
        private val caseInsensitive: Boolean by option("-i", help = "Case-insensitive search").flag()
        val numberOfLines: Int by option("-A", help = "Number of adjacent rows in the output").int().default(0)
        private val pattern: String by argument(help = "Pattern to find")
        val sources: List<String> by argument(help = "List of sources").multiple()
        override fun run() = Unit

        init {
            parse(rawArgs)
        }

        // Generating `Regex` object from the given arguments
        fun pattern(): Regex {
            val options = mutableSetOf<RegexOption>()
            if (caseInsensitive) {
                options.add(RegexOption.IGNORE_CASE)
            }
            return Regex(pattern = pattern, options = options)
        }
    }
}
