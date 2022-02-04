package ru.hse.shell.command

import ru.hse.shell.util.IO
import java.nio.file.Paths

class PwdCommand: Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        if (args.isNotEmpty()) {
            io.errorStream.write("pwd: too many arguments".toByteArray())
            return ExitCode.fail()
        }
        io.outputStream.write(Paths.get("").toAbsolutePath().toString().toByteArray())
        return ExitCode.success()
    }
}
