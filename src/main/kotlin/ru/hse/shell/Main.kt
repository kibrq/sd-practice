package ru.hse.shell

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import ru.hse.shell.handler.StatementHandler
import ru.hse.shell.parser.Parser
import ru.hse.shell.util.Environment
import ru.hse.shell.util.IO
import java.io.InputStream
import kotlin.system.exitProcess

private const val promptString = ">> "

private fun prompt(): String? {
    // Running from IntelliJ mixes stderr and stdout
    // https://youtrack.jetbrains.com/issue/IDEA-70016
    System.err.flush()
    Thread.sleep(100)
    System.out.flush()

    print(promptString)
    var input = readLine() ?: return null
    while (input.isBlank()) {
        print(promptString)
        input = readLine() ?: return null
    }
    return input.trim()
}


fun main() {
    val environment = Environment()
    val handler = StatementHandler()
    val io = IO(InputStream.nullInputStream(), System.out, System.err)
    while (true) {
        val input = prompt() ?: return
        val statement = try {
            Parser.parseToEnd(input)
        } catch (e: Exception) {
            System.err.println(e.message)
            continue
        }
        val exitCode = handler.handle(statement, environment, io)
        if (exitCode.isExit) {
            exitProcess(exitCode.code)
        }
    }
}
