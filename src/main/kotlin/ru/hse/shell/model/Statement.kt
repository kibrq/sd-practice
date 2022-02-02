package ru.hse.shell.model

sealed class Statement {
    class Assignment(val name: String, val value: String) : Statement()
    class RawCommand(val arguments: List<String>) : Statement()
}
