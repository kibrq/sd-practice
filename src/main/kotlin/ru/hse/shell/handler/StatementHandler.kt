package ru.hse.shell.handler

import ru.hse.shell.command.CommandRepository
import ru.hse.shell.model.EvalString
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

    private fun executeAssignment(environment: Environment, name: String, value: EvalString): ExitCode {
        environment.put(name, value.eval(environment))
        return ExitCode.success()
    }

    private fun executeCommand(environment: Environment, statement: Statement.RawCommand, io: IO): ExitCode {
        val arguments = statement.arguments.map { it.eval(environment) }
        val command = commandRepository.getCommand(arguments.first(), environment)
        return command.perform(arguments.drop(1), io)
    }
}
