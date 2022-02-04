package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

class EchoCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        var isFirst = true
        for (arg in args) {
            if (isFirst) {
                isFirst = false
            } else {
                io.outputStream.write(" ".toByteArray())
            }
            io.outputStream.write(arg.toByteArray())
        }
        return ExitCode.success()
    }
}
