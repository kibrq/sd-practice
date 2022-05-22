package ru.hse.core.checker

enum class CheckerVerdict {
    YES,
    NO
}

data class Checker(
    val dockerfile: String,
    val imageIdentifier: String
)
