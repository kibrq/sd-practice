package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils

/*
 * ExternalCommand represents tries to find a command with the given name on the computer.
 */
class ExternalCommand(private val commandName: String, private val env: Environment) : Command {
    /*
     * Execute external command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO): ExitCode {
        val processBuilder = ProcessBuilder().apply {
            command(commandName)
            command().addAll(args)
            environment().putAll(env.getAll())
        }

        return try {
            val process = processBuilder.start()
            io.inputStream.transferTo(process.outputStream)
            process.waitFor()
            process.inputStream.transferTo(io.outputStream)
            process.errorStream.transferTo(io.errorStream)
            ExitCode(process.exitValue(), false)
        } catch (e: Exception) {
            StreamUtils.writeToStream(io.errorStream, e.message)
            ExitCode.fail()
        }
    }
}
