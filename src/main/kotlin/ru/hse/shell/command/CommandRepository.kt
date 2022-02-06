package ru.hse.shell.command

import ru.hse.shell.util.Environment

/*
 * CommandRepository is a storage that helps map a string name of the basic command and a class that perfroms it.
 */
class CommandRepository {
    private val commandsMap: Map<String, Command> = hashMapOf(
        "wc" to WcCommand(),
        "cat" to CatCommand(),
        "pwd" to PwdCommand(),
        "echo" to EchoCommand(),
        "exit" to ExitCommand()
    )

    /*
     * Returns a Command with the given name, configuring it with the given env in case of external command.
     * If a basic command is not found in the repository, an external command is returned.
     */
    fun getCommand(name: String, env: Environment): Command {
        return commandsMap[name] ?: ExternalCommand(name, env)
    }
}
