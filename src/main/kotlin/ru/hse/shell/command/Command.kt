package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

/*
 * Represents a command: basic or external, that can be performed with given arguments and IO and return an ExitCode.
 */
@FunctionalInterface
interface Command {
    /*
     * Execute the command with given arguments and IO and return an ExitCode.
     */
    fun perform(args: List<String>, io: IO, env: Environment): ExitCode
}
