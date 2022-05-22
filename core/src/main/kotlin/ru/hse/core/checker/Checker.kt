package ru.hse.core.checker

enum class CheckerVerdict(val value: Boolean) {
    YES(true),
    NO(false);

    companion object {
        fun valueOf(value: Boolean): CheckerVerdict {
            return if (value) YES
            else NO
        }
    }
}

data class Checker(
    val dockerfile: String,
    val imageIdentifier: String
)
