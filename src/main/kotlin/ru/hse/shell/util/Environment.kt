package ru.hse.shell.util

import java.nio.file.Path
import java.nio.file.Paths

/*
 * Environment is a storage for shell's environment variables.
 */
class Environment {
    private val variables: MutableMap<String, String> = mutableMapOf()
    private var currentDirectory = Paths.get(System.getProperty("user.dir"))

    /*
     * Returns the value of the environment variable with given name.
     */
    fun get(name: String): String = variables.getOrDefault(name, "")

    /*
     * Puts a new environment variable with given name and value.
     * Overrides previous value if it exists.
     */
    fun put(name: String, value: String) = variables.put(name, value)

    /*
     * Returns an immutable copy of all the stored environment variables.
     */
    fun getAll(): Map<String, String> = variables

    /*
    * Returns path of directory in which shell executes commands
    */
    fun getCurrentDirectory(): Path = currentDirectory

    /*
    * Sets current directory to user's home
    */
    fun restoreCurrentDirectory() {
        currentDirectory = Paths.get(System.getProperty("user.home"))
    }

    /*
    * Sets current directory to parameter path
    */
    fun changeCurrentDirectory(path: String) {
        currentDirectory = currentDirectory.resolve(path)
    }

    /*
    * Resolves current working directory with path
    */
    fun resolveCurrentDirectory(path: String): Path {
        return currentDirectory.resolve(path)
    }

}
