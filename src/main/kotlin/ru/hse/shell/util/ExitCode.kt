package ru.hse.shell.util

/*
 * ExitCode encapsulates an integer -- return code of a command or statement, and a flag -- do we need to exit the app.
 */
data class ExitCode(val code: Int, val doExit: Boolean) {
    companion object {
        /*
         * A successful exit code.
         */
        fun success() = ExitCode(0, false)

        /*
         * An exit code telling that the app needs to be exited.
         */
        fun exit() = ExitCode(0, true)

        /*
         * A fail exit code with code 1.
         */
        fun fail() = ExitCode(1, false)

        /*
         * Returns succeeded exit code or failed exit code based on the given boolean.
         */
        fun finish(succeed: Boolean) = if (succeed) success() else fail()
    }
}
