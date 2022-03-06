package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils

/*
 * Bash's 'cat' analogue: displays the content of the files, listed in the arguments.
 */
class CatCommand : Command {
    /*
     * Execute the 'cat' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        return when {
            args.isEmpty() -> performWithNoArgs(io)
            else -> performWithArgs(args, io, env)
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

    private fun performWithArgs(args: List<String>, io: IO, env: Environment): ExitCode {
        var succeed = true
        for (fileName in args) {
            try {
                val filePath = env.resolveCurrentDirectory(fileName)
                filePath.toFile().inputStream().buffered().use {
                    it.transferTo(io.outputStream)
                }
            } catch (e: Exception) {
                succeed = false
                StreamUtils.writeToStream(io.errorStream, e.message)
            }
        }
        return ExitCode.finish(succeed)
    }
}
