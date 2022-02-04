package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.IO


class ExternalCommand(private val commandName: String, private val env: Environment):Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        val processBuilder = ProcessBuilder().apply {
            command(commandName)
            command().addAll(args)
            environment().putAll(env.variables)
        }
        return try {
            val process = processBuilder.start()
            io.inputStream.transferTo(process.outputStream)
            process.waitFor()
            process.inputStream.transferTo(io.outputStream)
            process.errorStream.transferTo(io.errorStream)
            ExitCode(process.exitValue(), false)
        } catch (e: Exception) {
            e.message?.let {
                io.errorStream.write(it.toByteArray())
            }
            ExitCode.fail()
        }
    }
}
