package ru.hse.shell.handler

import ru.hse.shell.command.CommandRepository
import ru.hse.shell.model.Statement
import ru.hse.shell.util.Environment
import ru.hse.shell.util.ExitCode
import ru.hse.shell.util.IO

/*
 * StatementHandler handles Statement objects, executing them with given environment and io.
 */
class StatementHandler {
    private val commandRepository = CommandRepository()

    /*
     * Executes given statement with given environment and io.
     */
    fun handle(statement: Statement, environment: Environment, io: IO): ExitCode {
        return when (statement) {
            is Statement.Assignment -> executeAssignment(environment, statement.name, statement.value)
            is Statement.RawCommand -> executeCommand(environment, statement, io)
        }
    }

    private fun executeAssignment(environment: Environment, name: String, value: String): ExitCode {
        environment.put(name, value)
        return ExitCode.success()
    }

    private fun executeCommand(environment: Environment, statement: Statement.RawCommand, io: IO): ExitCode {
        val command = commandRepository.getCommand(statement.arguments.first(), environment)
        return command.perform(statement.arguments.drop(1), io)
    }
}
