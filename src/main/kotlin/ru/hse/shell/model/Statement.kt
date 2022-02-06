package ru.hse.shell.model

// Класс, описывающий сущность `Statement` который может быть либо `Assignment` либо `RawCommand`.
// Часть грамматики

sealed class Statement {
    class Assignment(val name: String, val value: String) : Statement()
    class RawCommand(val arguments: List<String>) : Statement()
}
