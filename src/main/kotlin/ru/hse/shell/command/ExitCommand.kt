package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

/*
 * Bash's 'exit' analogue: exits the application.
 */
class ExitCommand : Command {
    /*
     * Execute the 'exit' command with given arguments and IO and return an ExitCode.
     */
    override fun perform(args: List<String>, io: IO): ExitCode {
        return ExitCode.exit()
    }
}
