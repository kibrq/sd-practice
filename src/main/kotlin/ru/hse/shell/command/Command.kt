package ru.hse.shell.command

import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

@FunctionalInterface
interface Command {
    fun perform(args: List<String>, io: IO): ExitCode
}
