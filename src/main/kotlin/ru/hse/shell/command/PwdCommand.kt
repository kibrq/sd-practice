package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils

/*
 * Bash's 'pwd' analogue: prints working directory.
 */
class PwdCommand : Command {
    /*
     * Execute the 'pwd' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        StreamUtils.writeToStream(io.outputStream, env.getCurrentDirectory().toAbsolutePath().toString())
        return ExitCode.success()
    }
}
