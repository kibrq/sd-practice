package ru.hse.shell.util

class Environment {
    private val variables: MutableMap<String, String> = mutableMapOf()

    fun get(name: String): String? = variables[name]

    fun put(name: String, value: String) = variables.put(name, value)

    fun getAll(): Map<String, String> = variables
}
