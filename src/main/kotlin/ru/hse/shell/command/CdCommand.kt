package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import kotlin.io.path.isDirectory

/*
 * Bash's 'cd' analogue: move between directories.
 */
class CdCommand : Command {
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        args.ifEmpty {
            env.restoreCurrentDirectory()
            return ExitCode.success()
        }

        if (args.size == 1) {
            val newPath = env.resolveCurrentDirectory(args[0])
            if (!newPath.isDirectory()) {
                return ExitCode.fail()
            }
            env.changeCurrentDirectory(args[0])
        } else {
            return ExitCode.fail()
        }

        return ExitCode.success()
    }
}
