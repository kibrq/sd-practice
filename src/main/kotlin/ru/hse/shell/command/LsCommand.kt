package ru.hse.shell.command

import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

/*
 * Bash's 'ls' analogue: list content of directory.
 */
class LsCommand : Command {

    override fun perform(args: List<String>, io: IO, env: Environment): ExitCode {
        val curPath = Paths.get("").toAbsolutePath()
        val paths = Files.walk(curPath, 1).map { curPath.relativize(it).toString() }.filter {
            it.isNotEmpty() &&  !it.startsWith(".")
        }.toList()
        StreamUtils.writeToStream(io.outputStream, paths.joinToString(separator = "\n"))
        return ExitCode.success()
    }

}
