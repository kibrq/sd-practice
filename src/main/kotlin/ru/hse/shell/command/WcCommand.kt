package ru.hse.shell.command

import org.apache.commons.io.IOUtils
import ru.hse.shell.util.IO
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


class WcCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        return when {
            args.isEmpty() -> performWithEmptyArgsList(io)
            else -> performWithNotEmptyArgsList(args, io)
        }
    }


    private fun performWithEmptyArgsList(io: IO): ExitCode {
        return try {
            val bytes =  IOUtils.toString(io.inputStream, StandardCharsets.UTF_8)
            val lines = bytes.split("\n")
            val numberOfRows = lines.size
            val numberOfBytes = bytes.length
            var numberOfWords = 0
            lines.forEach {
                numberOfWords += it.split(" ").filter { word -> word.isNotEmpty() }.size
            }
            io.outputStream.write("$numberOfRows $numberOfWords $numberOfBytes total".toByteArray())
            return ExitCode.success()
        } catch (e: Exception) {
            e.message?.let {
                io.errorStream.write(it.toByteArray())
            }
            ExitCode.fail()
        }
    }

    private fun performWithNotEmptyArgsList(args: List<String>, io: IO): ExitCode {
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
                io.outputStream.write("$numberOfRows $numberOfWords $numberOfBytes $fileName".toByteArray())
                if (args.size > 1) {
                    io.outputStream.write("\n".toByteArray())
                }
                totalNumberOfBytes += numberOfBytes
                totalNumberOfRows += numberOfRows
                totalNumberOfWords += numberOfWords
            } catch (e: Exception) {
                failHappened = true
                e.message?.let {
                    io.errorStream.write(it.toByteArray())
                }
            }
        }
        if (args.size > 1) {
            io.outputStream.write("$totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes total".toByteArray())
        }
        return if (failHappened) ExitCode.fail() else ExitCode.success()
    }
}
