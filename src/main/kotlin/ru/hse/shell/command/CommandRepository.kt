package ru.hse.shell.command

import ru.hse.shell.util.Environment

class CommandRepository {
    private val commandsMap: Map<String, Command> = hashMapOf(
        "wc" to WcCommand(),
        "cat" to CatCommand(),
        "pwd" to PwdCommand(),
        "echo" to EchoCommand(),
        "exit" to ExitCommand()
    )

    fun getCommand(name: String, env: Environment): Command {
        return commandsMap[name] ?: ExternalCommand(name, env)
    }
}
