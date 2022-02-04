package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import java.nio.file.Paths

class PwdCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        io.outputStream.write(Paths.get("").toAbsolutePath().toString().toByteArray())
        return ExitCode.success()
    }
}
