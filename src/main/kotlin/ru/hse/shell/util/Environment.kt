package ru.hse.shell.util

/*
 * Environment is a storage for shell's environment variables.
 */
class Environment {
    private val variables: MutableMap<String, String> = mutableMapOf()

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
}
