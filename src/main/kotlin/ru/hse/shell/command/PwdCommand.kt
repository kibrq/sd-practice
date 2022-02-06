package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.nio.file.Paths

class PwdCommand : Command {
    /*
     * Execute the 'pwd' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO): ExitCode {
        StreamUtils.writeToStream(io.outputStream, Paths.get("").toAbsolutePath().toString())
        return ExitCode.success()
    }
}
