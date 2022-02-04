package ru.hse.shell.command


data class ExitCode(val exitCode: Int, val isExit: Boolean) {
    companion object {
        fun success(): ExitCode {
            return ExitCode(0, false)
        }
        fun exit(): ExitCode {
            return ExitCode(0, true)
        }
        fun fail(): ExitCode {
            return ExitCode(1, false)
        }
    }
}
