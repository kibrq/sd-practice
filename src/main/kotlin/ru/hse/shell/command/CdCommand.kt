package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import kotlin.io.path.isDirectory

/*
 * Bash's 'cd' analogue: move between directories.
 */
class CdCommand : Command {

    /*
    * Execute the 'cd' command with given arguments and IO and return an ExitCode.
    */
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        args.ifEmpty {
            env.restoreCurrentDirectory()
            return ExitCode.success()
        }

        if (args.size == 1) {
            val newPath = env.resolveCurrentDirectory(args[0])
            if (!newPath.isDirectory()) {
                StreamUtils.writeToStream(io.errorStream, "cd: no such file or directory: " + args[0])
                return ExitCode.fail()
            }
            env.changeCurrentDirectory(args[0])
        } else {
            return ExitCode.fail()
        }

        return ExitCode.success()
    }
}
