package ru.hse.shell.command

import ru.hse.shell.util.IO
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
            val bytes = io.inputStream.readAllBytes()
            val lines = bytes.toString().split(System.lineSeparator().toRegex())
            val numberOfRows = lines.size
            val numberOfBytes = bytes.size
            var numberOfWords = 0
            lines.forEach {
                numberOfWords += it.split(" ").size
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
            val numberOfBytes = Files.size(Paths.get(fileName))
            try {
                Files.lines(Paths.get(fileName)).forEach {
                    numberOfRows++
                    numberOfWords += it.split(" ").size
                }
                io.outputStream.write("$numberOfRows $numberOfWords $numberOfBytes $fileName".toByteArray())
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
        io.outputStream.write("$totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes total".toByteArray())
        return if (failHappened) ExitCode.fail() else ExitCode.success()
    }
}
