package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

/*
 * Bash's 'ls' analogue: list content of directory.
 */
class LsCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        val curPath = Paths.get("").toAbsolutePath().toString()
        List<String> files = File(curPath).walk()
    }
}
