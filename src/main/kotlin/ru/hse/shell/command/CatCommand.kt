package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
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
            e.message?.let {
                io.errorStream.write(it.toByteArray())
            }
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
                        io.outputStream.write("\n".toByteArray())
                    }
                    it.transferTo(io.outputStream)
                }
            } catch (e: Exception) {
                failHappened = true
                e.message?.let {
                    io.errorStream.write(it.toByteArray())
                }
            }
        }
        return if (failHappened) ExitCode.fail() else ExitCode.success()
    }
}
