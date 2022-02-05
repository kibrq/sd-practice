package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

class ExitCommand : Command {
    override fun perform(args: List<String>, io: IO): ExitCode {
        return ExitCode.exit()
    }
}
