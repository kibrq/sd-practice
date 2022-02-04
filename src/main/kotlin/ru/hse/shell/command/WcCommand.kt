package ru.hse.shell.command

import org.apache.commons.io.IOUtils
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class WcCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        return when {
            args.isEmpty() -> performWithNoArgs(io)
            else -> performWithArgs(args, io)
        }
    }

    private fun performWithNoArgs(io: IO): ExitCode {
        return try {
            val bytes = IOUtils.toString(io.inputStream, StandardCharsets.UTF_8)
            val lines = bytes.split("\n")
            val numberOfRows = lines.size
            val numberOfBytes = bytes.length
            var numberOfWords = 0
            lines.forEach {
                numberOfWords += it.split(" ").filter { word -> word.isNotEmpty() }.size
            }
            StreamUtils.writeToStream(io.outputStream, "$numberOfRows $numberOfWords $numberOfBytes total")
            ExitCode.success()
        } catch (e: Exception) {
            StreamUtils.writeToStream(io.errorStream, e.message)
            ExitCode.fail()
        }
    }

    private fun performWithArgs(args: List<String>, io: IO): ExitCode {
        var failHappened = false
        var totalNumberOfRows = 0L
        var totalNumberOfWords = 0L
        var totalNumberOfBytes = 0L
        for (fileName in args) {
            var numberOfRows = 0
            var numberOfWords = 0
            var numberOfBytes = 0
            try {
                Files.lines(Paths.get(fileName)).forEach {
                    numberOfRows++
                    numberOfWords += it.split(" ").filter { word -> word.isNotEmpty() }.size
                    numberOfBytes += it.length
                }
                numberOfBytes += numberOfRows - 1

                val message = "$numberOfRows $numberOfWords $numberOfBytes $fileName"
                StreamUtils.writeToStream(io.outputStream, message, args.size > 1)

                totalNumberOfBytes += numberOfBytes
                totalNumberOfRows += numberOfRows
                totalNumberOfWords += numberOfWords
            } catch (e: Exception) {
                failHappened = true
                StreamUtils.writeToStream(io.errorStream, e.message)
            }
        }
        if (args.size > 1) {
            val message = "$totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes total"
            StreamUtils.writeToStream(io.outputStream, message)
        }
        return if (failHappened) ExitCode.fail() else ExitCode.success()
    }
}
