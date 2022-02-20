package ru.hse.shell.handler

import ru.hse.shell.model.Statement
import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO
import ru.hse.shell.util.StreamUtils
import java.io.PipedInputStream
import java.io.PipedOutputStream

/*
 * ExpressionHandler executes Expressions(Statement lists) objects as pipes with given environment and io.
 */
class ExpressionHandler {
    private val statementHandler = StatementHandler()

    /*
     * Executes given Expression(Statement list) with given environment and io.
     */
    fun handle(statements: List<Statement>, environment: Environment, io: IO): ExitCode {
        return try {
            var code = ExitCode.success()
            var nextInput = io.inputStream
            for (statement in statements) {
                val out = PipedOutputStream()
                val currentIO = IO(nextInput, out, io.errorStream)
                nextInput = PipedInputStream()
                out.connect(nextInput)

                code = statementHandler.handle(statement, environment, currentIO)
                out.close()
                if (code.doExit) {
                    break
                }
            }
            nextInput.transferTo(io.outputStream)
            code
        } catch (e: Exception) {
            StreamUtils.writeToStream(io.errorStream, e.message)
            ExitCode.fail()
        }
    }
}
