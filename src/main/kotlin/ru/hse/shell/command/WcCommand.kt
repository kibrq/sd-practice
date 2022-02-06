package ru.hse.shell.command

import org.apache.commons.io.IOUtils
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class WcCommand : Command {
    /*
     * Execute the 'wc' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO): ExitCode {
        return when {
            args.isEmpty() -> performWithNoArgs(io)
            else -> performWithArgs(args, io)
        }
    }

    private fun performWithNoArgs(io: IO): ExitCode {
        return try {
            val content = IOUtils.toString(io.inputStream, StandardCharsets.UTF_8)
            val wc = computeWordCount(content)
            val message = "${wc.rowsCount} ${wc.wordsCount} ${wc.bytesCount} total"
            StreamUtils.writeToStream(io.outputStream, message)
            ExitCode.success()
        } catch (e: Exception) {
            StreamUtils.writeToStream(io.errorStream, e.message)
            ExitCode.fail()
        }
    }

    private fun performWithArgs(args: List<String>, io: IO): ExitCode {
        var succeed = true
        var totalRowsCount = 0L
        var totalWordsCount = 0L
        var totalBytesCount = 0L
        for (fileName in args) {
            try {
                val content = Files.readString(Paths.get(fileName))
                val wc = computeWordCount(content)

                val message = "${wc.rowsCount} ${wc.wordsCount} ${wc.bytesCount} $fileName"
                StreamUtils.writeToStream(io.outputStream, message)

                totalRowsCount += wc.rowsCount
                totalWordsCount += wc.wordsCount
                totalBytesCount += wc.bytesCount
            } catch (e: Exception) {
                succeed = false
                StreamUtils.writeToStream(io.errorStream, e.message)
            }
        }
        if (args.size > 1) {
            val message = "$totalRowsCount $totalWordsCount $totalBytesCount total"
            StreamUtils.writeToStream(io.outputStream, message)
        }
        return ExitCode.finish(succeed)
    }

    private data class WordCount(val rowsCount: Int, val wordsCount: Int, val bytesCount: Int)

    private fun computeWordCount(content: String): WordCount {
        val lines = content.lines()
        var numberOfRows = lines.size
        if (lines.last().isEmpty()) {
            numberOfRows--
        }
        val numberOfWords = content.split(Regex("\\s")).count { word -> word.isNotEmpty() }
        val numberOfBytes = content.length
        return WordCount(numberOfRows, numberOfWords, numberOfBytes)
    }
}
