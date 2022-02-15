package ru.hse.shell.command


import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

class GrepCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        val config = parseArgs(args)
        return ExitCode.success()
    }

    private data class GrepConfig(
        val findAsWord: Boolean,
        val caseInsensitive: Boolean,
        val numberOfLines: Int,
        val pattern: String,
        val sources: List<String>
    )

    private class GrepArgsParser : CliktCommand() {
        val findAsWord: Boolean by option("-w", help = "Find as word").flag()
        val caseInsensitive: Boolean by option("-i", help = "Case-insensitive search").flag()
        val numberOfLines: Int by option("-A", help = "Number of adjacent rows in the output").int().default(0)
        val pattern: String by argument(help = "Pattern to find")
        val sources: List<String> by argument(help = "List of sources").multiple()
        override fun run() {
            //We only need the result of parsing as GrepConfig
        }

        fun getConfig(): GrepConfig {
            return GrepConfig(findAsWord, caseInsensitive, numberOfLines, pattern, sources)
        }
    }

    private fun parseArgs(args: List<String>): GrepConfig {
        val parser = GrepArgsParser()
        parser.parse(args)
        return parser.getConfig()
    }

}
