package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils

class EchoCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        for (arg in args.dropLast(1)) {
            StreamUtils.writeToStream(io.outputStream, "$arg ", addNewline = false)
        }
        StreamUtils.writeToStream(io.outputStream, args.last())
        return ExitCode.success()
    }
}
