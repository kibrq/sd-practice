package ru.hse.shell.util


data class ExitCode(val code: Int, val isExit: Boolean) {
    companion object {
        fun success() = ExitCode(0, false)

        fun exit() = ExitCode(0, true)

        fun fail() = ExitCode(1, false)

        fun finish(succeed: Boolean) = if (succeed) success() else fail()
    }
}
