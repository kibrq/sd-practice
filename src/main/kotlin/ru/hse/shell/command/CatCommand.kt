package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.io.File

class CatCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        return when {
            args.isEmpty() -> performWithNoArgs(io)
            else -> performWithArgs(args, io)
        }
    }

    private fun performWithNoArgs(io: IO): ExitCode {
        return try {
            io.inputStream.transferTo(io.outputStream)
            ExitCode.success()
        } catch (e: Exception) {
            StreamUtils.writeToStream(io.errorStream, e.message)
            ExitCode.fail()
        }
    }

    private fun performWithArgs(args: List<String>, io: IO): ExitCode {
        var failHappened = false
        var isFirst = true
        for (fileName in args) {
            try {
                File(fileName).inputStream().buffered().use {
                    if (isFirst) {
                        isFirst = false
                    } else {
                        StreamUtils.writeToStream(io.outputStream, "", addNewline = true)
                    }
                    it.transferTo(io.outputStream)
                }
            } catch (e: Exception) {
                failHappened = true
                StreamUtils.writeToStream(io.errorStream, e.message)
            }
        }
        return if (failHappened) ExitCode.fail() else ExitCode.success()
    }
}
