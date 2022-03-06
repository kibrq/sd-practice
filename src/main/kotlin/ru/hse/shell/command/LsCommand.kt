package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

/*
 * Bash's 'ls' analogue: list content of directory.
 */
class LsCommand : Command {
    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        TODO("Not yet implemented")
    }
}
